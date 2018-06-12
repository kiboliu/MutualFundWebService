package com.DeltaMutualFund.DeltaMutualFund.controller;

import com.DeltaMutualFund.DeltaMutualFund.domain.*;
import com.DeltaMutualFund.DeltaMutualFund.helper.FundHelper;
import com.DeltaMutualFund.DeltaMutualFund.helper.PriceList;
import com.DeltaMutualFund.DeltaMutualFund.repository.*;
import com.DeltaMutualFund.DeltaMutualFund.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransitionController {

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
    
    @GetMapping("/employee/TransitionDay")
    public String transitionDay(Model model) {
        List<Fund> fundlist = fundRepository.findAll();
        PriceList priceList = new PriceList();
        List<FundHelper> list = new ArrayList<>();
        for (Fund fund : fundlist) {
            List<Funddetails> funddetails = funddetailsRepository.findByFundIdentityFundId(fund.getId());
            FundHelper fundHelper = null;
            if (funddetails.size() == 0) {
                fundHelper = new FundHelper(fund.getName(), fund.getSymbol(), "", 0.001);
            } else {
                double pastprice = funddetails.get(funddetails.size() - 1).getFundprice();
                fundHelper = new FundHelper(fund.getName(), fund.getSymbol(), "", pastprice);
            }
            list.add(fundHelper);
        }
        priceList.setPriceList(list);

        List<Funddetails> funddetails_list = funddetailsRepository.findAll();
        Funddetails funddetails_last = funddetails_list.get(funddetails_list.size()-1);
        Fundidentity fundidentity_last = funddetails_last.getFundIdentity();
        Date date_last = fundidentity_last.getPriceDate();
        model.addAttribute("previousDate", date_last);
        model.addAttribute("wrapper", priceList);
        model.addAttribute("error","");
        return "employees/TransitionDay";
    }

    public String transitionError(Model model, List<String> errors, PriceList pricelist) {
        List<Fund> fundlist = fundRepository.findAll();
        PriceList priceList = new PriceList();
        List<FundHelper> list = new ArrayList<>();
        for (Fund fund : fundlist) {
            List<Funddetails> funddetails = funddetailsRepository.findByFundIdentityFundId(fund.getId());
            FundHelper fundHelper = null;
            if (funddetails.size() == 0) {
                fundHelper = new FundHelper(fund.getName(), fund.getSymbol(), "", 0.001);
            } else {
                double pastprice = funddetails.get(funddetails.size() - 1).getFundprice();
                fundHelper = new FundHelper(fund.getName(), fund.getSymbol(), "", pastprice);
            }
            list.add(fundHelper);
        }
        priceList.setPriceList(list);

        List<Funddetails> funddetails_list = funddetailsRepository.findAll();
        Funddetails funddetails_last = funddetails_list.get(funddetails_list.size()-1);
        Fundidentity fundidentity_last = funddetails_last.getFundIdentity();
        Date date_last = fundidentity_last.getPriceDate();
        model.addAttribute("previousDate", date_last);

        for (int i = 0; i < pricelist.getPriceList().size(); i++) {
            priceList.getPriceList().get(i).setPrice(pricelist.getPriceList().get(i).getPrice());
        }
        model.addAttribute("wrapper", priceList);
        model.addAttribute("error", errors);
        return "employees/TransitionDay";
    }
    /**
     * Transition Day Edit
     * @param priceList
     * @param date
     * @return
     */
    @PostMapping("/employee/TransitionDay")
    @Transactional
    public String transitionDay(@ModelAttribute PriceList priceList, @RequestParam(required=false, name="exe_date") String date, Model model) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date validdate = new Date(dateFormat.parse(date).getTime());
            List<Funddetails> funddetails_list = funddetailsRepository.findAll();
            Funddetails funddetails_last = funddetails_list.get(funddetails_list.size() - 1);
            Fundidentity fundidentity_last = funddetails_last.getFundIdentity();
            Date date_last = fundidentity_last.getPriceDate();
            List<String> errors = new ArrayList<>();
            if (date_last.getTime() >= validdate.getTime()) {
                errors.add("Please set date after the previous trading day.");
                return transitionError(model, errors, priceList);
            }
            if (priceList.validate().length() > 0) {
                errors.add(priceList.validate());
            }
            List<FundHelper> prices = priceList.getPriceList();
            List<Fund> funds = fundRepository.findAll();

            if (prices.size() != funds.size()) {
                errors.add("Please set prices for all funds.");
                return transitionError(model, errors, priceList);
            }
            for (int i = 0; i < prices.size(); i++) {
                Fund fund = funds.get(i);
                List<Funddetails> funddetails = funddetailsRepository.findByFundIdentityFundId(fund.getId());
                if (funddetails.size() != 0) {
                    double pastprice = funddetails.get(funddetails.size() - 1).getFundprice();
                    double currentprice = Double.parseDouble(prices.get(i).getPrice().replace(",", ""));
                    if (currentprice < pastprice * 0.8) {
                        errors.add("Please set new price NOT below 20% to the last price.");
                        break;
                    }
                    if (currentprice > pastprice * 1.2) {
                        errors.add("Please set new price NOT exceed 20% to the last price.");
                        break;
                    }
                }
            }
            if (errors.size() > 0) {
                return transitionError(model, errors, priceList);
            }
            int len = prices.size();
            for (int i = 0; i < len; i++) {
                Fundidentity fundidentity = new Fundidentity(funds.get(i).getId(), validdate);
                double updateprice = Double.parseDouble(prices.get(i).getPrice().replace(",",""));
                Funddetails funddetails = new Funddetails(fundidentity, updateprice);
                funddetailsRepository.save(funddetails);
            }
            List<Transaction> transactions = transactionRepository.findTransactionsByStatus("Pending");
            for (Transaction transaction : transactions) {
                User user = userRepository.findOne(transaction.getCus_id());
                if (transaction.getTransactiontype().equals("Deposit Check")) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    double amount = Double.parseDouble(decimalFormat.format(transaction.getAmount()));
                    System.out.println(amount);
                    user.setCash(user.getCash() + amount);
                    transaction.setExe_date(validdate);
                    transaction.setStatus("Success");
                    userRepository.save(user);
                    transactionRepository.save(transaction);
                } else if (transaction.getTransactiontype().equals("Request Check")) {
                    if (user.getCash() < transaction.getAmount()) {
                        transaction.setStatus("Reject");
                        transactionRepository.save(transaction);
                        continue;
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                   double amount = Double.parseDouble(decimalFormat.format(transaction.getAmount()));
                    System.out.println(amount);
                    user.setCash(user.getCash() - amount);
                    transaction.setExe_date(validdate);
                    transaction.setStatus("Success");
                    userRepository.save(user);
                    transactionRepository.save(transaction);
                } else if (transaction.getTransactiontype().equals("Sell")) {
                    Fundidentity fundIdentity = new Fundidentity(transaction.getFund_id(), validdate);
                    Funddetails funddetails = funddetailsRepository.findOne(fundIdentity);
                    double sellPrice = funddetails.getFundprice();

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    double changeAmount = Double.parseDouble(decimalFormat.format(sellPrice * transaction.getShares()));
                    if (changeAmount == 0.0) {
                        transaction.setStatus("Reject");
                        transactionRepository.save(transaction);
                        continue;
                    }
                    user.setCash(user.getCash() + changeAmount);
                    user.setAvailablebalance(user.getAvailablebalance() + changeAmount);
                    transaction.setAmount(changeAmount);
                    transaction.setExe_date(validdate);
                    transaction.setStatus("Success");
                    userRepository.save(user);
                    transactionRepository.save(transaction);
                } else if (transaction.getTransactiontype().equals("Buy")) {
                    if (user.getCash() < transaction.getAmount()) {
                        transaction.setStatus("Reject");
                        transactionRepository.save(transaction);
                        continue;
                    }
                    Fundidentity fundIdentity = new Fundidentity(transaction.getFund_id(), validdate);
                    Funddetails funddetails = funddetailsRepository.findOne(fundIdentity);
                    double sellPrice = funddetails.getFundprice();
                    DecimalFormat decimalFormat = new DecimalFormat("0.000");
                    double tmpres = transaction.getAmount() / sellPrice;
                    tmpres = Math.round(tmpres * 1000.000) / 1000.000;
                    double changeShare = Double.parseDouble(decimalFormat.format(tmpres));
                    if (changeShare == 0.0) {
                        transaction.setStatus("Reject");
                        transactionRepository.save(transaction);
                        continue;
                    }
                    Positionidentity positionidentity = new Positionidentity(user.getId(), transaction.getFund_id());
                    Position position = positionRepository.findOne(positionidentity);
                    if (position == null) {
                        Position positionnew = new Position(positionidentity, changeShare);
                        positionRepository.save(positionnew);
                    } else {
                        position.setFundshares(position.getFundshares() + changeShare);
                        positionRepository.save(position);
                    }
                    user.setCash(user.getCash() - transaction.getAmount());
                    transaction.setShares(changeShare);
                    transaction.setExe_date(validdate);
                    transaction.setStatus("Success");
                    transactionRepository.save(transaction);
                    userRepository.save(user);
                }
            }
            return "redirect:/employee";
        } catch (ParseException e) {
            List<String> errors = new ArrayList<>();
            errors.add("Lack input date/price or invalid input types.");
            return transitionError(model, errors, priceList);
        }
    }
}
