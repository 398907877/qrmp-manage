package com.bwton.qrmp.manage.vo.operatorManagement;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.bwton.qrmp.manage.vo.contacts.ContactsVo;

import lombok.Data;


@Data
public class OperatorInformation  implements  Serializable{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column g_operator_information.ID
     *
     * @mbg.generated
     */
	
	
	private  List<ContactsVo>    linkmans;
	
	
    private Integer id;
    
    private String province_id;
    
    private String city_id;
    

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column g_operator_information.Operator_name
     *
     * @mbg.generated
     */
    private String operatorName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column g_operator_information.location
     *
     * @mbg.generated
     */
    private String location;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column g_operator_information.operator_desc
     *
     * @mbg.generated
     */
    private String operatorDesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column g_operator_information.Service_state
     *
     * @mbg.generated
     */
    private String serviceState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column g_operator_information.last_modify_user
     *
     * @mbg.generated
     */
    private String lastModifyUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column g_operator_information.last_modify_time
     *
     * @mbg.generated
     */
    private Date lastModifyTime;
    
    

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column g_operator_information.ID
     *
     * @return the value of g_operator_information.ID
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column g_operator_information.ID
     *
     * @param id the value for g_operator_information.ID
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column g_operator_information.Operator_name
     *
     * @return the value of g_operator_information.Operator_name
     *
     * @mbg.generated
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column g_operator_information.Operator_name
     *
     * @param operatorName the value for g_operator_information.Operator_name
     *
     * @mbg.generated
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column g_operator_information.location
     *
     * @return the value of g_operator_information.location
     *
     * @mbg.generated
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column g_operator_information.location
     *
     * @param location the value for g_operator_information.location
     *
     * @mbg.generated
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column g_operator_information.operator_desc
     *
     * @return the value of g_operator_information.operator_desc
     *
     * @mbg.generated
     */
    public String getOperatorDesc() {
        return operatorDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column g_operator_information.operator_desc
     *
     * @param operatorDesc the value for g_operator_information.operator_desc
     *
     * @mbg.generated
     */
    public void setOperatorDesc(String operatorDesc) {
        this.operatorDesc = operatorDesc == null ? null : operatorDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column g_operator_information.Service_state
     *
     * @return the value of g_operator_information.Service_state
     *
     * @mbg.generated
     */
    public String getServiceState() {
        return serviceState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column g_operator_information.Service_state
     *
     * @param serviceState the value for g_operator_information.Service_state
     *
     * @mbg.generated
     */
    public void setServiceState(String serviceState) {
        this.serviceState = serviceState == null ? null : serviceState.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column g_operator_information.last_modify_user
     *
     * @return the value of g_operator_information.last_modify_user
     *
     * @mbg.generated
     */
    public String getLastModifyUser() {
        return lastModifyUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column g_operator_information.last_modify_user
     *
     * @param lastModifyUser the value for g_operator_information.last_modify_user
     *
     * @mbg.generated
     */
    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser == null ? null : lastModifyUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column g_operator_information.last_modify_time
     *
     * @return the value of g_operator_information.last_modify_time
     *
     * @mbg.generated
     */
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column g_operator_information.last_modify_time
     *
     * @param lastModifyTime the value for g_operator_information.last_modify_time
     *
     * @mbg.generated
     */
    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}