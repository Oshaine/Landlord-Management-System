/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Oshaine
 */
public interface PaymentsInterface {

    double getAmountDue();

    Date getNextPayDate();

    Date getLastPayDate();

    double getLateFee();

    int getTotalDaysLate();

    double getDiscAmount();

    double getChange();

    double getOutsAmtount();

    void setAmountDue(double amountDue);

    void setNextPayDate(Date nextPayDate);

    void setLastPayDate(Date lastPayDate);

    void setLateFee(double lateFee);

    void setTotalDaysLate(int totalDaysLate);

    void setDiscAmount(double discAmount);

    void setChange(double change);

    void setOutsAmtount(double outsAmount);

}
