/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import interfaces.ChargesInterface;
import interfaces.DiscountInterface;
import interfaces.PaymentsInterface;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author Oshaine
 */
public class Payments implements PaymentsInterface, ChargesInterface, Serializable {

    private double amountDue;
    private Date nextPayDate;
    private Date lastPayDate;
    private double lateFee;
    private int totalDaysLate;
    private double discAmount;
    private double change;
    private double outsAmtount;
    private double cash;
    private static final long serialVersionUID = 3458489886428771425L;

    /**
     * @return the amountDue
     */
    @Override
    public double getAmountDue() {
        return amountDue;
    }

    /**
     * @param amountDue the amountDue to set
     */
    @Override
    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    /**
     * @return the nextPayDate
     */
    @Override
    public Date getNextPayDate() {
        return nextPayDate;
    }

    /**
     * @param nextPayDate the nextPayDate to set
     */
    @Override
    public void setNextPayDate(Date nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    /**
     * @return the lastPayDate
     */
    @Override
    public Date getLastPayDate() {
        return lastPayDate;
    }

    /**
     * @param lastPayDate the lastPayDate to set
     */
    @Override
    public void setLastPayDate(Date lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    /**
     * @return the lateFee
     */
    @Override
    public double getLateFee() {
        return lateFee;
    }

    /**
     * @param lateFee the lateFee to set
     */
    @Override
    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    /**
     * @return the totalDaysLate
     */
    @Override
    public int getTotalDaysLate() {
        return totalDaysLate;
    }

    /**
     * @param totalDaysLate the totalDaysLate to set
     */
    @Override
    public void setTotalDaysLate(int totalDaysLate) {
        this.totalDaysLate = totalDaysLate;
    }

    /**
     * @return the discAmount
     */
    @Override
    public double getDiscAmount() {
        return discAmount;
    }

    /**
     * @param discAmount the discAmount to set
     */
    @Override
    public void setDiscAmount(double discAmount) {
        this.discAmount = discAmount;
    }

    /**
     * @return the change
     */
    @Override
    public double getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    @Override
    public void setChange(double change) {
        this.change = change;
    }

    /**
     * @return the outsAmtount
     */
    @Override
    public double getOutsAmtount() {
        return outsAmtount;
    }

    /**
     * @param outsAmtount the outsAmtount to set
     */
    @Override
    public void setOutsAmtount(double outsAmtount) {
        this.outsAmtount = outsAmtount;
    }

    /*This method accepts a double and converts it to monetary values with $ sign
and formatted with comma separator to two decimal places.*/
    public String moneyFormat(double money) {
        String cashAmount;
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        cashAmount = currencyFormatter.format(money);

        return cashAmount;
    }

    /*This Method accepts a Tenant when registered then calculate their next
    payment date based on the day that they have been registered and payment made*/
    public Date nextPayDate(Tenant tenant) {
        GregorianCalendar calendar = new GregorianCalendar();
        Date newDate = null;
        if (tenant.getPayments().getLastPayDate() == null) {
            calendar.add(Calendar.DAY_OF_WEEK, ChargesInterface.BILLING_CYCLE); //28 days
        }
        newDate = calendar.getTime();
        tenant.getPayments().setNextPayDate(newDate);

        return newDate;
    }

    /*This Method accepts a Tenant when registered then calculate their Amount Due
    based on their gender and the charges prior to week days and weekends*/
    
    public Tenant calAmtDue(Tenant tenant) {
        calNumDays days = new calNumDays(nextPayDate(tenant));
        int numWeekDays = days.getCountWkDays();
        int numWeekEnds = days.getCountWKEnds();
        double amtDue = 0;
        double outStandingAmt = tenant.getPayments().getOutsAmtount();
        double weekDayAmt = numWeekDays * ChargesInterface.FLATRATE;
        double weekEndAmt = numWeekEnds * ChargesInterface.FLATRATE;
        if (tenant.getGender().equals("Female")) {
            amtDue = weekDayAmt + weekEndAmt + outStandingAmt;
            tenant.getPayments().setDiscAmount(DiscountInterface.FEMALE * amtDue);
            amtDue -= tenant.getPayments().getDiscAmount();

        } else {
            amtDue = weekDayAmt + weekEndAmt + outStandingAmt;
        }

        amtDue += tenant.getPayments().getLateFee();

        //reset
        tenant.getPayments().setAmountDue(amtDue);

        return tenant;
    }

    //inner class
    private final class calNumDays {

        int weekDays;
        int weekEnds;

        //weekdays
        void setCountWkDays(int weekDays) {
            this.weekDays = weekDays;
        }

        //weekEnds
        void setCountWkEnds(int weekEnds) {
            this.weekEnds = weekEnds;
        }

        //get WeekDays
        int getCountWkDays() {
            return this.weekDays;
        }

        //get weekEndDays
        int getCountWKEnds() {
            return this.weekEnds;
        }

        /*This Method accepts a the tenant next payment date and caluclate the number
        of saturdays and sundays within the month */
        
        public calNumDays(Date nextDate) {
            GregorianCalendar startCal = new GregorianCalendar();
            GregorianCalendar endCal = new GregorianCalendar();
            endCal.setTime(nextDate);
            int countWkDays = 0;
            int countWkEnds = 0;
            do {
                if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                        || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    ++countWkEnds;
                } else if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                        && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++countWkDays;
                }
                //excluding start date
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
            //set inner class weekend days and weekdays late
            setCountWkDays(countWkDays);
            setCountWkEnds(countWkEnds);
        }
    }

    
    /*This method accepts a tenant and checks if their payment date
    is before or after the next payment to calculate their late fee 
    charges based on the amount of late days prior to the late fee rate per day*/
    public void calLateFee(Tenant tenant) {
        if (tenant.getPayments().getNextPayDate() != null) {
            GregorianCalendar now = new GregorianCalendar();

            GregorianCalendar nextpayDate = new GregorianCalendar();
            nextpayDate.setTime(tenant.getPayments().getNextPayDate());
            int countWkEnds = 0;
            int countWkDays = 0;
            if (nextpayDate.getTime().after(now.getTime())) {
                //evaulate
                do {
                    if (nextpayDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                            || nextpayDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        ++countWkEnds;
                    } else if (nextpayDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                            && nextpayDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        ++countWkDays;
                    }
                    //excluding start date
                    nextpayDate.add(Calendar.DAY_OF_WEEK, 1);
                } while (nextpayDate.getTimeInMillis() > now.getTimeInMillis());

            }
            //add .75% to each late dat
            double LateDaysFee = (countWkDays * ChargesInterface.FLATRATE)
                    + ((countWkDays * ChargesInterface.FLATRATE) * ChargesInterface.LATEFEE);

            double LateWKendFee = (countWkEnds * ChargesInterface.FLATRATE)
                    + ((countWkEnds * ChargesInterface.WEEKENDRATE) * ChargesInterface.LATEFEE);
            //total late fee
            double totalLateFee = LateDaysFee + LateWKendFee;
            //total days late
            int daysLate = countWkDays + countWkEnds;
            tenant.getPayments().setLateFee(totalLateFee);
            tenant.getPayments().setTotalDaysLate(daysLate);
        }
    }

    /**
     * @return the cash
     */
    public double getCash() {
        return cash;
    }

    /**
     * @param cash the cash to set
     */
    public void setCash(double cash) {
        this.cash = cash;
    }
}
