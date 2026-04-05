package hanhuynh.com.shipmentservice.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Calculate delivery date based on
 * 1. Start Date
 * 2. Processing time
 * 3. Shipping time
 * 4. Holiday
 */
@Service
public class DeliveryDateCalculator {

	@Value("${shipment.processing-days}")
    private int processingDays;
 
    @Value("${shipment.shipping-days}")
    private int shippingDays;
 
    // list of Holiday in Vietnam (MM-dd)
    private static final Set<String> PUBLIC_HOLIDAYS = Set.of(
        "01-01", // New Year's Day
        "04-30", // Reunification Day (Liberation of the South)
        "05-01", // International Workers' Day
        "09-02"  // National Day (Vietnam Independence Day)
    );
 
    /**
     * Calculate the estimated delivery date based on the order creation date
     *
     * @param orderDate
     * @return estimated delivery date
     */
    public LocalDate calculate(LocalDate orderDate) {
        int totalWorkingDays = processingDays + shippingDays;
        LocalDate deliveryDate = orderDate;
        int daysAdded = 0;
 
        while (daysAdded < totalWorkingDays) {
            deliveryDate = deliveryDate.plusDays(1);
 
            // Skip weekends and public holidays
            if (isWorkingDay(deliveryDate)) {
                daysAdded++;
            }
        }
 
        return deliveryDate;
    }
 
    private boolean isWorkingDay(LocalDate date) {
        if (isWeekend(date))   return false;
        if (isHoliday(date))   return false;
        return true;
    }
 
    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
 
    private boolean isHoliday(LocalDate date) {
        String monthDay = String.format("%02d-%02d", date.getMonthValue(), date.getDayOfMonth());
        return PUBLIC_HOLIDAYS.contains(monthDay);
    }
}
