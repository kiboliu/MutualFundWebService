package com.DeltaMutualFund.DeltaMutualFund.controller;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.DeltaMutualFund.DeltaMutualFund.domain.Authority;
import com.DeltaMutualFund.DeltaMutualFund.domain.DepCheckHelper;
import com.DeltaMutualFund.DeltaMutualFund.domain.Employee;
import com.DeltaMutualFund.DeltaMutualFund.domain.Fund;
import com.DeltaMutualFund.DeltaMutualFund.domain.Funddetails;
import com.DeltaMutualFund.DeltaMutualFund.domain.Fundidentity;
import com.DeltaMutualFund.DeltaMutualFund.domain.Position;
import com.DeltaMutualFund.DeltaMutualFund.domain.Positionidentity;
import com.DeltaMutualFund.DeltaMutualFund.domain.Transaction;
import com.DeltaMutualFund.DeltaMutualFund.domain.User;
import com.DeltaMutualFund.DeltaMutualFund.helper.FundHelper;
import com.DeltaMutualFund.DeltaMutualFund.helper.PriceList;
import com.DeltaMutualFund.DeltaMutualFund.repository.EmployeeRepository;
import com.DeltaMutualFund.DeltaMutualFund.repository.FundRepository;
import com.DeltaMutualFund.DeltaMutualFund.repository.FunddetailsRepository;
import com.DeltaMutualFund.DeltaMutualFund.repository.PositionRepository;
import com.DeltaMutualFund.DeltaMutualFund.repository.TransactionRepository;
import com.DeltaMutualFund.DeltaMutualFund.repository.UserRepository;
import com.DeltaMutualFund.DeltaMutualFund.service.UserService;
import com.DeltaMutualFund.DeltaMutualFund.util.ConstraintViolationExceptionHandler;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private FundRepository fundRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FunddetailsRepository funddetailsRepository;
    @Autowired
    private PositionRepository positionRepository;

    @GetMapping
    public ModelAndView eDashboard(Model model) {
        model.addAttribute("title", "Employee Dashboard");
        return new ModelAndView("employees/EmployeeDashboard", "employeeModel", model);
    }

    @GetMapping("/DepositCheck")
    public ModelAndView depositCheck(Model model) {
        List<User> customerList= userRepository.findAll();
        String errorMsg = "";
        DepCheckHelper check = new DepCheckHelper(null, null);
        model.addAttribute("customerList", customerList);
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("check", check);
        return new ModelAndView("employees/DepositCheck", "transModel", model);
    }

    @Transactional
    @PostMapping("/DepositCheck")
    public ModelAndView depositCheck(@RequestParam("amount") String amount,
            @RequestParam("username") String username, Model model, RedirectAttributes redirectAttrs) {
        List<String> errors = new ArrayList<>();
        DepCheckHelper check = new DepCheckHelper(username, amount);
        model.addAttribute("check", check);
        if ( username == null || username.isEmpty()) {
            errors.add("Username should not be empty");
        }

        User forCus = userRepository.findByUsername(username);
        if (forCus == null) {
            errors.add("User not found");
        }

        if (amount == null || amount.isEmpty()) {
            errors.add("Amount should not be empty");
        }

        double number = 0;
        try {
            amount = amount.replaceAll(",", "");
            number = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            errors.add("Amount should only be a number");
        }

        if ( number == 0 ) {
            errors.add("Amount should not be 0");
        }
        if ( number <= 0.99 || number >= 999999.99 ) {
            errors.add("Amount should be greater than 1.00 & less than 1,000,000");
        }
        if (!errors.isEmpty()) {
            List<User> customerList = userRepository.findAll();
            model.addAttribute("errorMsg", errors);
            model.addAttribute("customerList", customerList);
            return new ModelAndView("employees/DepositCheck", "transModel", model);
        }
        double newBalance = forCus.getAvailablebalance() + number;
        forCus.setAvailablebalance(newBalance);
        Transaction transaction = new Transaction();
        transaction.setCus_id(forCus.getId());
        transaction.setAmount(number);
        transaction.setTransactiontype("Deposit Check");
        transaction.setStatus("Pending");
        transactionRepository.save(transaction);
        

        redirectAttrs.addAttribute("id", forCus.getId());
        return new ModelAndView("redirect:/employee/CustomerAccounts/{id}");

    }
    
    @GetMapping("/CreateFund")
    public ModelAndView createFund(Model model) {
        model.addAttribute("newFund", new Fund(null, null));
        model.addAttribute("fundList", fundRepository.findAll());
        model.addAttribute("title", "Create New Available Fund");
        List<String> createFundError = new ArrayList<String>();
        String successMsg = "";
        model.addAttribute("createFundErrorMsg", createFundError);
        model.addAttribute("successMsg", successMsg);
        return new ModelAndView("employees/CreateFund", "fundModel", model);
    }

    @PostMapping("/CreateFund")
    @Transactional
    public ModelAndView createFund(@RequestParam(required = false, name = "name") String fundname,
            @RequestParam(required = false, name = "symbol") String symbol,
            Fund fund, Model model) {
        List<String> createFundError = new ArrayList<String>();
        String successMsg = "";
        model.addAttribute("newFund", new Fund(fundname, symbol));
        model.addAttribute("createFundErrorMsg", createFundError);
        model.addAttribute("successMsg", successMsg);
        model.addAttribute("fundList", fundRepository.findAll());
        model.addAttribute("title", "Create New Available Fund");
        List<String> fundNameList = new ArrayList<String> ();
        List<Fund> allFund = fundRepository.findAll();
        List<String> symbolList = new ArrayList<String> ();
        for (Fund eachFund: allFund) {
            fundNameList.add(eachFund.getName());
            symbolList.add(eachFund.getSymbol());
        }
       
        
        if (fundname == null || fundname.isEmpty()) {
            createFundError.add("Fund name can not be empty!");
        } else {
            if (fundNameList.contains(fundname)) {
                createFundError.add("Fund name must be unique!");
            }
        }
        
        if (symbol == null || symbol.isEmpty()) {
            createFundError.add("Symbol can not be empty!");
        } else {
            if (symbolList.contains(symbol)) {
                createFundError.add("Symbol must be unique!");
            } else {
                if (!symbol.matches("[a-zA-Z]{1,5}")) {
                    createFundError.add("Symbol must be a one to five character only strings!");
                }
            }
        }
        
        if (createFundError.isEmpty()) {
            successMsg = "Fund Create Success!";
            fundRepository.save(fund);
            return new ModelAndView("redirect:/employee/CreateFund");
        } else {
            return showError(createFundError, model, "employees/CreateFund", "fundModel");
        }
        
    }

    /**
     * Helper method to handle error
     * @param errorMsg
     * @param model
     * @return
     */
    public ModelAndView showError(List<String> errorMsg, Model model, String html, String modelName) {
        model.addAttribute("errorMsg", errorMsg);
        return new ModelAndView(html, modelName, model);
    }

    @GetMapping("/CreateEmployee")
    public ModelAndView createEAccount(Model model) {
        model.addAttribute("newEmployee", new Employee(null, null, null, null));
        model.addAttribute("title", "Create Employee Account");
        String errorMsg = "";
        model.addAttribute("errorMsg", errorMsg);
        return new ModelAndView("employees/CreateEmployeeAccount", "employeeModel", model);
    }

    @PostMapping("/CreateEmployee")
    @Transactional
    public ModelAndView createEAccount(@RequestParam(required = false, name = "username") String username,
            @RequestParam(required = false, name = "firstname") String firstname,
            @RequestParam(required = false, name = "lastname") String lastname,
            @RequestParam(required = false, name = "password") String password, Employee employee, Model model) {
            model.addAttribute("title", "Create Employee Account");
            model.addAttribute("newEmployee", new Employee(username, firstname, lastname, password));
            List<String> errorList = new ArrayList<String>();
            List<String> employeeNameList = new ArrayList<String> ();
            List<Employee> allEmployee = employeeRepository.findAll();
            for (Employee eachEmployee: allEmployee) {
                employeeNameList.add(eachEmployee.getUsername());
            }
            List<String> customerNameList = new ArrayList<String> ();
            List<User> allUser = userRepository.findAll();
            for (User eachUser: allUser) {
                customerNameList.add(eachUser.getUsername());
            }
            
            
            if (username == null || username.isEmpty()) {
                errorList.add("Employee username can not be empty!");
            } else {
                if (username.length() > 20 || username.length() < 1) {
                    errorList.add("Employee username must between 1 and 20 characters!");
                    }
                if (employeeNameList.contains(username)) {
                    errorList.add("Employee username must be unique!");
                    }
                if (customerNameList.contains(username)) {
                    errorList.add("Employee username must be different from customer username!");
                    }
                }                   
            
            if (firstname == null || firstname.isEmpty()) {
                errorList.add("Employee first name can not be empty!");
            } else {
                if (firstname.length() > 100 || firstname.length() < 1) {
                    errorList.add("Employee first name must between 1 and 100 characters!");
                    }
                }
            
            if (lastname == null || lastname.isEmpty()) {
                errorList.add("Employee last name can not be empty!");
            } else {
                if (lastname.length() > 100 || lastname.length() < 1) {
                    errorList.add("Employee last name must between 1 and 100 characters!");
                    }
                }
                    
            if (password == null || password.isEmpty()) {
                errorList.add("Employee password can not be empty!");
            } else {
                if (password.length() > 100 || password.length() < 3) {
                    errorList.add("Employee last name must between 3 and 100 characters!");
                    }
                } 

            if (errorList.isEmpty()) {
                List<Authority> authorities = new ArrayList<>();
                Authority authority = new Authority();
                authority.setId(2L);
                authority.setName("ADMIN");
                authorities.add(authority);
                employee.setAuthorities(authorities);
                employeeRepository.save(employee);
                return new ModelAndView("redirect:/employee/ViewEmployee");
            } else {
                return showError(errorList, model, "employees/CreateEmployeeAccount", "employeeModel");
            }
    }

    @GetMapping("/ViewEmployee")
    public ModelAndView viewEmployee(Model model) {
        model.addAttribute("EmployeeList", employeeRepository.findAll());
        model.addAttribute("title", "View Employee Information");
        return new ModelAndView("employees/ViewEmployee", "employeeModel", model);
    }
    
    @PostMapping("/modifyEmployeePassword")
    @Transactional
    public ModelAndView saveOrUpdateUser(Employee employee) {
        Employee current = employeeRepository.findByUsername(employee.getUsername());
        current.setPassword(employee.getPassword());
        employeeRepository.save(current);
        return new ModelAndView("redirect:/employee/ViewEmployee");
    }

    @GetMapping("/ChangeEmployeePassword/{username}")
    public ModelAndView changeEPassword(@PathVariable("username") String username, Model model) {
        Employee employee = employeeRepository.findOne(username);
        model.addAttribute("modifyEmployee", employee);
        model.addAttribute("title", "Change Employee Password");
//      return new ModelAndView("employees/CreateEmployeeAccount", "employeeModel", model);
        return new ModelAndView("employees/ChangeEmployeePassword", "employeeModel", model);
    }

    @GetMapping("/CreateCustomer")
    public ModelAndView createCAccount(Model model) {
        model.addAttribute("newCustomer", new User(null, null, null, null, null, null, null, null, null, 0.00, 0.00));
        model.addAttribute("title", "Create Customer Account");
        List <String> newList = new ArrayList<String> ();
        model.addAttribute("cusErrorList", newList);
        return new ModelAndView("employees/CreateCustomerAccount", "employeeModel", model);
    }

    @PostMapping("/CreateCustomer")
    @Transactional
    public ModelAndView createCAccount(@RequestParam(required = false, name = "username") String username,
            @RequestParam(required = false, name = "password") String password,
            @RequestParam(required = false, name = "firstname") String firstname,
            @RequestParam(required = false, name = "lastname") String lastname,
            @RequestParam(required = false, name = "addr_line1") String addr_line1,
            @RequestParam(required = false, name = "addr_line2") String addr_line2,
            @RequestParam(required = false, name = "city") String city,
            @RequestParam(required = false, name = "state") String state,
            @RequestParam(required = false, name = "zip") String zip,
            User user, Model model) {
        List<String> customerErrorList = new ArrayList<>();
        List<String> employeeNameList = new ArrayList<String> ();
        List<Employee> allEmployee = employeeRepository.findAll();
        for (Employee eachEmployee: allEmployee) {
            employeeNameList.add(eachEmployee.getUsername());
        }
        List<String> customerNameList = new ArrayList<String> ();
        List<User> allUser = userRepository.findAll();
        for (User eachUser: allUser) {
            customerNameList.add(eachUser.getUsername());
        }
        model.addAttribute("cusErrorList", customerErrorList);
        model.addAttribute("title", "Create Customer Account");
        model.addAttribute("newCustomer", new User(username, password, firstname, 
                lastname, addr_line1, addr_line2, city, state, zip, 0.00, 0.00));
        if (username == null || username.isEmpty()) {
            customerErrorList.add("User name can not be empty!");
        }
        if (password == null || password.isEmpty()) {
            customerErrorList.add("Customer password can not be empty!");
        }
        if (firstname == null || firstname.isEmpty()) {
            customerErrorList.add("Customer first name can not be empty!");
        }
        if (lastname == null || lastname.isEmpty()) {
            customerErrorList.add("Customer last name can not be empty!");
        }
        if (addr_line1 == null || addr_line1.isEmpty()) {
            customerErrorList.add("Customer address line 1 can not be empty!");
        }
        if (city == null || city.isEmpty()) {
            customerErrorList.add("City can not be empty!");
        }
        if (state == null || state.isEmpty()) {
            customerErrorList.add("State can not be empty!");
        }
        if (zip == null || zip.isEmpty()) {
            customerErrorList.add("Zip code can not be empty!");
        }
        if (!customerErrorList.isEmpty()) {
            model.addAttribute("cusErrorList", customerErrorList);
            model.addAttribute("title", "Create Customer Account");
            model.addAttribute("newCustomer", new User(username, password, firstname, lastname, 
                    addr_line1, addr_line2, city, state, zip, 0.00, 0.00));
            return new ModelAndView("employees/CreateCustomerAccount", "employeeModel", model);
        }
        else {
            if (username.length() > 20 || username.length() < 1) {
                customerErrorList.add("Customer name must between 1 and 20 characters!");
            }
            if (employeeNameList.contains(username)) {
                customerErrorList.add("Customer username must be different from employee username!");
                }
            if (customerNameList.contains(username)) {
                customerErrorList.add("Customer username must be unique!");
                }
            if (password.length() > 100 || password.length() < 3) {
                customerErrorList.add("Customer name must between 3 and 100 characters!");
            }
            if (firstname.length() > 100 || firstname.length() < 1) {
                customerErrorList.add("Customer first name must between 1 and 100 characters!");
            }
            if (lastname.length() > 100 || lastname.length() < 1) {
                customerErrorList.add("Customer last name must between 1 and 100 characters!!");
            }
            if (addr_line1.length() > 100 || addr_line1.length() < 1) {
                customerErrorList.add("Customer address line 1 must between 1 and 100 characters!!");
            }
            if (city.length() > 100 || city.length() < 1) {
                customerErrorList.add("City must between 1 and 100 characters!!");
            }
            if (state.length() != 2) {
                customerErrorList.add("State must be 2 characters!!");
            }
            if (zip.length() != 5) {
                customerErrorList.add("Zip code must be 5 characters!!");
            }
            if (!customerErrorList.isEmpty()) {
                model.addAttribute("cusErrorList", customerErrorList);
                model.addAttribute("title", "Create Customer Account");
                model.addAttribute("newCustomer", new User(username, password, firstname, lastname, addr_line1, addr_line2, city, state, zip, 0.00, 0.00));
                return new ModelAndView("employees/CreateCustomerAccount", "employeeModel", model);
            }
        }
        List<Authority> authorities = new ArrayList<>();
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName("USER");
        authorities.add(authority);
        user.setAuthorities(authorities);
        userRepository.save(user);

        return new ModelAndView("redirect:/employee/ViewCustomer");
    }


    @GetMapping("/ViewCustomer")
    public ModelAndView viewCustomer(Model model) {
        model.addAttribute("CustomerList", userRepository.findAll());
        model.addAttribute("title", "View Customer Information");
        return new ModelAndView("employees/ViewCustomer", "employeeModel", model);
    }


    /**
     * Customer info including transaction history
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/CustomerAccounts/{id}")
    public ModelAndView viewCustomerInfo(@PathVariable("id") Long id, Model model) {
        model.addAttribute("title", "View Individual Customer Information");
        User curt = userService.getUserById(id);
        model.addAttribute("curt", curt);
        List<Transaction> transList = transactionRepository.findTransactionsByCusid(id);
        model.addAttribute("transList", transList);
        return new ModelAndView("employees/CustomerTrans", "customerModel", model);
    }

    /**
     * Modify the current customer
     * @return
     */
    @PostMapping("/modifyCustomer")
    @Transactional
    public ModelAndView saveOrUpdateUser(User user) {
        User current = userRepository.findByUsername(user.getUsername());
        current.setPassword(user.getPassword());
        userRepository.save(current);
        return new ModelAndView("redirect:/employee/ViewCustomer");
    }

    /**
     * get page for modify user password
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/modify/{id}")
    public ModelAndView modify(@PathVariable("id") Long id, Model model) {
        User curt = userService.getUserById(id);
        model.addAttribute("curt", curt);
        model.addAttribute("title", "Modify Customer");
        return new ModelAndView("employees/ModifyCustomerAccount","customerModel",model);
    }
}
