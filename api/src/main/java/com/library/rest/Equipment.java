package com.library.rest;

import java.util.Date;

/**
 *  Equipment document
 */
public final class Equipment {
    private String equipmentNumber;
    private String address;
    private Date contractStartDate;
    private Date contractEndDate;
    private String status;
    private String _id;
    private String _rev;

    public Equipment() {}

    /**
     * IoT Equipment
     *
     * @param equipmentNumber
     * @param address
     * @param contractStartDate
     * @param contractEndDate
     * @param status
     */
    public Equipment(String equipmentNumber, String address, Date contractStartDate, Date contractEndDate, String status)  {
        this.equipmentNumber = equipmentNumber;
        this.address = address;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.status = status;
    }

    /**
     * Validates Equipment Number
     *
     * @param equipmentNumber
     * @return boolean
     */
    public static boolean isValidEquipmentNumber(String equipmentNumber) {
        return equipmentNumber != null && equipmentNumber.trim().length() > 0 & equipmentNumber.trim().length() <= 10;
    }

    /**
     * Equipment object must have valid Equipment Number
     *
     * @return boolean
     */
    public final boolean isValid() {
        return isValidEquipmentNumber(this.equipmentNumber);
    }

    /**
     * IoT Equipment identifying number
     *
     * @return String n
     */
    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    /**
     * IoT Equipment IP address v4
     *
     * @return String n.n.n.n
     */
    public String getAddress() {
        return address;
    }

    /**
     * IoT Equipment constract starting date
     *
     * @return Date
     */
    public Date getContractStartDate() {
        return contractStartDate;
    }

    /**
     * IoT Equipment contract starting date
     *
     * @return Date
     */
    public Date getContractEndDate() {
        return contractEndDate;
    }

    /**
     * IoT Equipment status
     *
     * @return String Running / Stopped
     */
    public String getStatus() {
        return status;
    }

    /**
     * IoT Equipment cloudant row id
     *
     * @return String _id
     */
    public String get_id() {
        return _id;
    }

    /**
     * IoT Equipment cloudant row revision
     *
     * @return String _rev
     */
    public String get_rev() {
        return _rev;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentNumber='" + equipmentNumber + '\'' +
                ", address='" + address + '\'' +
                ", contractStartDate=" + contractStartDate +
                ", contractEndDate=" + contractEndDate +
                ", status='" + status + '\'' +
                ", _id='" + _id + '\'' +
                ", _rev='" + _rev + '\'' +
                '}';
    }
}