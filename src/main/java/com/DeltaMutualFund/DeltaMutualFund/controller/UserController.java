package com.DeltaMutualFund.DeltaMutualFund.controller;

import com.DeltaMutualFund.DeltaMutualFund.domain.*;
import com.DeltaMutualFund.DeltaMutualFund.helper.PositionHelper;
import com.DeltaMutualFund.DeltaMutualFund.helper.SellHelper;
import com.DeltaMutualFund.DeltaMutualFund.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private FunddetailsRepository funddetailsRepository;

    @Autowired
    private PositionRepository positionRepository;


    /**
     * Home Page for User
     * @param principal
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView homePage(Principal principal, Model model, @RequestBody(required = false) List<String> errors) {
        User current = userRepository.findByUsername(principal.getName());
        List<Transaction> transactions = transactionRepository.findTransactionsByCusid(current.getId());
        boolean flg = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            if(transactions.get(i).getStatus().equals("Success")) {
                Date date = transactions.get(i).getExe_date();
                model.addAttribute("tradeday", date);
                flg = true;
                break;
            }
        }
        if (flg == false) model.addAttribute("tradeday", ' ');
        model.addAttribute("name", current.getFirstname() + " " +current.getLastname());
        model.addAttribute("address", current.getAddr_line2() + " " + current.getAddr_line1() + ", " +current.getCity() + ", " + current.getState() + ", " + current.getZip());
        model.addAttribute("balance", current.getCash());
        model.addAttribute("available", current.getAvailablebalance());
        List<PositionHelper> positionlist = new ArrayList<>();
        List<Position> list = positionRepository.findByPositionIdentityCusId(current.getId());
        for (Position position : list) {
            PositionHelper positionHelper = new PositionHelper();
            Positionidentity positionidentity = position.getPositionIdentity();
            positionHelper.setFundId(positionidentity.getFundId());
            Fund fund = fundRepository.findOne(positionidentity.getFundId());
            positionHelper.setFundName(fund.getName());
            positionHelper.setShares(position.getFundshares());
            List<Funddetails> funddetails = funddetailsRepository.findByFundIdentityFundId(positionidentity.getFundId());
            positionHelper.setPrice(funddetails.get(funddetails.size() - 1).getFundprice());
            positionlist.add(positionHelper);
        }
        model.addAttribute("positionlist", positionlist);
        if (errors == null) {
            errors = new ArrayList<>();
            errors.add(" ");
        }
        model.addAttribute("error", errors);
        return new ModelAndView("users/account","model",model);
    }

    /**
     * Change Password for User
     * @param password
     * @param confrimpw
     * @param principal
     * @param model
     * @return
     */
    @PostMapping
    @Transactional
    public ModelAndView changePassword(@RequestParam(required=false, name="password") String password, @RequestParam(required=false, name="confirmpw") String confrimpw, Principal principal, Model model) {
        try {
            List<String> errors = new ArrayList<>();
            if (password == null || confrimpw == null) {
                errors.add("Please input both password and confirm password.");
                return homePage(principal, model, errors);
            }
            if (!password.equals(confrimpw)) {
                errors.add("Password is not equal to confirmed password.");
                return homePage(principal, model, errors);
            }
            password.replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("&","&amp;");
            User current = userRepository.findByUsername(principal.getName());
            current.setPassword(password);
            userRepository.save(current);
            return new ModelAndView("redirect:/user");
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Please input valid password.");
            return homePage(principal, model, errors);
        }
    }

    /**
     * Buy fund
     * @return
     */
    @GetMapping("/buy")
    public ModelAndView buy(Principal principal, Model model, @RequestBody(required = false) List<String> errors) {
        User current = userRepository.findByUsername(principal.getName());
        model.addAttribute("cash", current.getCash());
        model.addAttribute("available", current.getAvailablebalance());
        model.addAttribute("fundlist", fundRepository.findAll());
        if (errors == null) {
            errors = new ArrayList<>();
            errors.add(" ");
        }
        model.addAttribute("error", errors);
        return new ModelAndView("users/buy_fund", "model",model);
    }

    @PostMapping("/buy")
    @Transactional
    public ModelAndView buyFund(@RequestParam(required=false, name="fund") String fund, @RequestParam(required=false, name="amount") String amount, Principal principal, Model model, HttpServletRequest request) {
        try {
            List<String> errors = new ArrayList<>();
            if (fund == null) {
                errors.add("Please choose a fund.");
            }
            if (amount == null) {
                errors.add("Please input the amount you require.");
            }
            double money = Double.parseDouble(amount);
            if (money <= 0) {
                errors.add("Please input positive number.");
            }
            Transaction transaction = new Transaction();
            User current = userRepository.findByUsername(principal.getName());
            if (current.getAvailablebalance() < money) {
                errors.add("You can not exceed the amouont limit.");
            }
            if (errors.size() > 0) {
                return buy(principal, model, errors);
            }
            current.setAvailablebalance(current.getAvailablebalance() - money);
            transaction.setCus_id(current.getId());
            transaction.setFund_id(fundRepository.findByName(fund).getId());
            transaction.setFund_name(fund);
            transaction.setTransactiontype("Buy");
            transaction.setAmount(money);
            transaction.setStatus("Pending");
            transactionRepository.save(transaction);
            return new ModelAndView("redirect:/user/transaction");
        } catch (NumberFormatException e) {
            List<String> errors = new ArrayList<>();
            errors.add("Invalid input format");
            return buy(principal, model, errors);
        }
    }

    /**
     * Sell fund
     * @return
     */
    @GetMapping("/sell")
    public ModelAndView sell(Model model, Principal principal, @RequestBody(required = false) List<String> errors) {
        model.addAttribute("selllist",getSelllist(principal));
        if (errors == null) {
            errors = new ArrayList<>();
            errors.add(" ");
        }
        model.addAttribute("error", errors);

        return new ModelAndView("users/sell_fund", "model", model);
    }

    @PostMapping("/sell")
    @Transactional
    public ModelAndView sellFund(@RequestParam(required=false, name="fund") String fund, @RequestParam(required=false, name="share") String share, Principal principal, Model model, HttpServletRequest request) {
        try {
            List<String> errors = new ArrayList<>();
            if (fund == null) {
                errors.add("Please choose a fund.");
            }
            if (share == null) {
                errors.add("Please input the number of fund you require.");
            }
            double money = Double.parseDouble(share);
            if (money <= 0) {
                errors.add("Please input a positive number of fund.");
            }
            if (errors.size() > 0) {
                return sell(model, principal, errors);
            }
            Transaction transaction = new Transaction();
            User current = userRepository.findByUsername(principal.getName());
            Fund thisfund = fundRepository.findByName(fund);
            List<Position> positions = positionRepository.findByPositionIdentityCusId(current.getId());
            for (Position position : positions) {
                Positionidentity positionidentity = position.getPositionIdentity();
                if (positionidentity.getFundId() == thisfund.getId()) {
                    double usershare = position.getFundshares();
                    if (usershare < money) {
                        errors.add("You can not sell shares that exceed your current shares.");
                        return sell(model, principal, errors);
                    }
                    if (money == usershare) {
                        positionRepository.delete(positionidentity);
                        break;
                    }
                    position.setFundshares(position.getFundshares() - money);
                    positionRepository.save(position);
                    break;
                }
            }
            transaction.setCus_id(current.getId());
            transaction.setFund_id(fundRepository.findByName(fund).getId());
            transaction.setTransactiontype("Sell");
            transaction.setFund_name(fund);
            transaction.setShares(money);
            transaction.setStatus("Pending");
            transactionRepository.save(transaction);
            return new ModelAndView("redirect:/user/transaction");
        } catch (NumberFormatException e) {
            List<String> errors = new ArrayList<>();
            errors.add("Invalid Input format");
            return sell(model, principal, errors);
        }
    }

    /**
     * Request Check
     * @return
     */
    @GetMapping("/check")
    public ModelAndView check(Principal principal, Model model, @RequestBody(required=false) List<String> errors) {

        User current = userRepository.findByUsername(principal.getName());
        model.addAttribute("cash", current.getCash());
        model.addAttribute("available", current.getAvailablebalance());
        if (errors == null) {
            errors = new ArrayList<>();
            errors.add(" ");
        }
        model.addAttribute("error", errors);
        return new ModelAndView("users/request_check", "model", model);

    }

    @PostMapping("/check")
    @Transactional
    public ModelAndView requestCheck(@RequestParam(required=false, name="amount") String amount, Principal principal, Model model) {
        try {
            List<String> errors = new ArrayList<>();
            if (amount == null) {
                errors.add("Please input a amount.");
            }
            double money = Double.parseDouble(amount);
            if (money <= 0) {
                errors.add("Please input a positive amount.");
            }
            User current = userRepository.findByUsername(principal.getName());
            if (current.getAvailablebalance() < money) {
                errors.add("You can not exceed your available balance.");
            }
            if (errors.size() > 0) {
                return check(principal, model, errors);
            }
            Transaction transaction = new Transaction();
            current.setAvailablebalance(current.getAvailablebalance() - money);
            transaction.setCus_id(current.getId());
            transaction.setTransactiontype("Request Check");
            transaction.setStatus("Pending");
            transaction.setAmount(money);
            transactionRepository.save(transaction);
            return new ModelAndView("redirect:/user/transaction");
        } catch (NumberFormatException e){
            List<String> errors = new ArrayList<>();
            errors.add("Invalid Input Format");
            return check(principal, model, errors);
        }
    }

    /**
     * View Transaction
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/transaction")
    public ModelAndView transaction(Model model, Principal principal) {
        User current = userRepository.findByUsername(principal.getName());
        List<Transaction> list = transactionRepository.findTransactionsByCusid(current.getId());
        Collections.reverse(list);
        model.addAttribute("transactions", list);
        return new ModelAndView("users/transaction_history", "model", model);
    }

    @GetMapping("/research")
    public ModelAndView research(Model model, @RequestBody(required = false) List<String> errors) {
        List<Fund> list = fundRepository.findAll();
        model.addAttribute("fundlist", list);
        model.addAttribute("dates",' ');
        model.addAttribute("prices",' ');
        model.addAttribute("display", null);
        model.addAttribute("fundname", ' ');
        if (errors == null) {
            errors = new ArrayList<>();
            errors.add(" ");
        }
        model.addAttribute("error", errors);
        return new ModelAndView("users/fund_research", "model", model);
    }

    @PostMapping("/research")
    @Transactional
    public ModelAndView researchFund(@RequestParam(required=false, name="fund") String fund, Model model, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        if (fund == null) {
            errors.add("Please input a positive amount.");
            return research(model, errors);
        }
        Fund thisfund = fundRepository.findByName(fund);
        List<Funddetails> funddetails = funddetailsRepository.findByFundIdentityFundId(thisfund.getId());
        List<String> dates = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        for (Funddetails funddetail : funddetails) {
            Fundidentity fundidentity = funddetail.getFundIdentity();
            dates.add(fundidentity.getPriceDate().toString());
            prices.add(funddetail.getFundprice());
        }
        List<Fund> list = fundRepository.findAll();
        model.addAttribute("fundlist", list);
        model.addAttribute("dates", dates);
        model.addAttribute("prices", prices);
        model.addAttribute("display", "block");
        model.addAttribute("fundname", fund);
        errors.add(" ");
        model.addAttribute("error", errors);
        return new ModelAndView("users/fund_research", "model", model);
    }

    public List<SellHelper> getSelllist(Principal principal) {
        User current = userRepository.findByUsername(principal.getName());
        List<Position> list = positionRepository.findByPositionIdentityCusId(current.getId());
        List<SellHelper> selllist = new ArrayList<>();
        for (Position position : list) {
            SellHelper sellHelper = new SellHelper();
            Positionidentity positionidentity = position.getPositionIdentity();
            Fund fund = fundRepository.findOne(positionidentity.getFundId());
            sellHelper.setFundname(fund.getName());

            sellHelper.setShares(position.getFundshares());
            selllist.add(sellHelper);
        }
        return selllist;
    }
}
