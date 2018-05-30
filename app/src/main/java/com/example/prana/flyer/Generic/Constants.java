package com.example.prana.flyer.Generic;

/**
 * Created by kanik on 3/29/2017.
 */

public class Constants {

    public static final String db_date_format="dd-MMM-yyyy";
    public static final String db_date_time_format="dd-MMM-yyyy HH:mm";
    public static final String monthly_sms_template="[VendorName]\nYour bill for [Month] [Year] is [Amount]. Please pay the bill  at the earliest. \n" +
            "Ignore this message if already paid.";
    public static final String instant_sms_template="[VendorName]\nYou have purchased following items on [PurchaseDate].\n" +
            "[ItemDetails]\n" +
            "Gross Amount: [TotalAmount]\nTax: [Tax]\nDiscount: [Discount]\nNet Amount: [GrossAmount]";

    //Preference's data.
    public static final String inventory_shared_prefrence="Inventory_Preference";
    public static final String vendor_name="Vendor_Name";
    public static final String selected_date_format="date_format";
    public static final String selected_time_format="time_format";
    public static final String selected_currency="currency";
    public static final String login_status="login_status";
    public static final String login_fb_status="login_fb_status";
    public static final String DOWNLOAD_IMAGE_PATH="activation_status";




}
