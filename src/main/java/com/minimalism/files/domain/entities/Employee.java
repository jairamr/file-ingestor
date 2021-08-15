package com.minimalism.files.domain.entities;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class Employee {
    private String empId;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String gender;
    private String emailId;
    private String fathersName;
    private String mothersName;
    private String mothersMaidenName;
    private String dateOfBirth;
    private String timeOfBirth;
    private double ageInYrs;
    private double weightInKgs;
    private String dateOfJoining;
    private String quarterOfJoining;
    private String halfOfJoining;
    private short yearOfJoining;
    private short monthOfJoining;
    private String monthOfJoiningName;
    private String monthOfJoiningShortname;
    private short dayOfJoining;
    private String dowOfJoining;
    private String dowOfJoiningShortname;
    private double ageInCompany;
    private double salary;
    private double lastHikePercent;
    private String ssn;
    private String phoneNumber;
    private String placeName;
    private String county;
    private String city;
    private String state;
    private String zip;
    private String region;
    private String username;
    private String password;

    /** 
     * @param fieldSeparator
     * @param commaSeparated
     */
    public void makeFromFile(String fieldSeparator, String commaSeparated) {
        String[] fields = commaSeparated.split(fieldSeparator);
        this.setEmpId(fields[0]);
        this.setNamePrefix(fields[1]);
        this.setFirstName(fields[2]);
        this.setMiddleInitial(fields[3]);
        this.setLastName(fields[4]);
        this.setGender(fields[5]);
        this.setEmailId(fields[6]);
        this.setFathersName(fields[7]);
        this.setMothersName(fields[8]);
        this.setMothersMaidenName(fields[9]);
        this.setDateOfBirth(fields[10]);
        this.setTimeOfBirth(fields[11]);
        this.setAgeInYrs(fields[12]);
        this.setWeightInKgs(fields[13]);
        this.setDateOfJoining(fields[14]);
        this.setQuarterOfJoining(fields[15]);
        this.setHalfOfJoining(fields[16]);
        this.setYearOfJoining(fields[17]);
        this.setMonthOfJoining(fields[18]);
        this.setMonthOfJoiningName(fields[19]);
        this.setMonthOfJoiningShortname(fields[20]);
        this.setDayOfJoining(fields[21]);
        this.setDowOfJoining(fields[22]);
        this.setDowOfJoiningShortname(fields[23]);
        this.setAgeInCompany(fields[24]);
        this.setSalary(fields[25]);
        this.setLastHikePercent(fields[26]);
        this.setSSN(fields[27]);
        this.setPhoneNumber(fields[28]);
        this.setPlaceName(fields[29]);
        this.setCounty(fields[30]);
        this.setCity(fields[31]);
        this.setState(fields[32]);
        this.setZip(fields[33]);
        this.setRegion(fields[34]);
        this.setUsername(fields[35]);
        this.setPassword(fields[36]);
    }

    public Employee(String fieldSeparator, String fromFile) {
        this.makeFromFile(fieldSeparator, fromFile);
    }

    
    /** 
     * @return String
     */
    public String getEmpId() {
        return empId;
    }
    
    /** 
     * @param empID
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    
    /** 
     * @return String
     */
    public String getNamePrefix() {
        return namePrefix;
    }
    
    /** 
     * @param namePrefix
     */
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
    
    /** 
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }
    
    /** 
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /** 
     * @return String
     */
    public String getMiddleInitial() {
        return middleInitial;
    }
    
    /** 
     * @param middleInitial
     */
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }
    
    /** 
     * @return String
     */
    public String getLastName() {
        return lastName;
    }
    
    /** 
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /** 
     * @return String
     */
    public String getGender() {
        return gender;
    }
    
    /** 
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /** 
     * @return String
     */
    public String getEmailId() {
        return emailId;
    }
    
    /** 
     * @param eMail
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    
    /** 
     * @return String
     */
    public String getFathersName() {
        return fathersName;
    }
    
    /** 
     * @param fathersName
     */
    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }
    
    /** 
     * @return String
     */
    public String getMothersName() {
        return mothersName;
    }
    
    /** 
     * @param mothersName
     */
    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }
    
    /** 
     * @return String
     */
    public String getMothersMaidenName() {
        return mothersMaidenName;
    }
    
    /** 
     * @param mothersMaidenName
     */
    public void setMothersMaidenName(String mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
    }
    
    /** 
     * @return String
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    /** 
     * @param dateOfBirth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    /** 
     * @return String
     */
    public String getTimeOfBirth() {
        return timeOfBirth;
    }
    
    /** 
     * @param timeOfBirth
     */
    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }
    
    /** 
     * @return double
     */
    public double getAgeInYrs() {
        return ageInYrs;
    }
    
    /** 
     * @param ageInYrs
     */
    public void setAgeInYrs(int ageInYrs) {
        this.ageInYrs = ageInYrs;
    }
    
    /** 
     * @param ageInYrs
     */
    public void setAgeInYrs(String ageInYrs) {
        this.ageInYrs = Double.valueOf(ageInYrs);
    }
    
    /** 
     * @return double
     */
    public double getWeightInKgs() {
        return weightInKgs;
    }
    
    /** 
     * @param weightInKgs
     */
    public void setWeightInKgs(double weightInKgs) {
        this.weightInKgs = weightInKgs;
    }
    
    /** 
     * @param weightInKgs
     */
    public void setWeightInKgs(String weightInKgs) {
        this.weightInKgs = Double.valueOf(weightInKgs);
    }
    
    /** 
     * @return String
     */
    public String getDateOfJoining() {
        return dateOfJoining;
    }
    
    /** 
     * @param dateOfJoining
     */
    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }
    
    /** 
     * @return String
     */
    public String getQuarterOfJoining() {
        return quarterOfJoining;
    }
    
    /** 
     * @param quarterOfJoining
     */
    public void setQuarterOfJoining(String quarterOfJoining) {
        this.quarterOfJoining = quarterOfJoining;
    }
    
    /** 
     * @return String
     */
    public String getHalfOfJoining() {
        return halfOfJoining;
    }
    
    /** 
     * @param halfOfJoining
     */
    public void setHalfOfJoining(String halfOfJoining) {
        this.halfOfJoining = halfOfJoining;
    }
    
    /** 
     * @return short
     */
    public short getYearOfJoining() {
        return yearOfJoining;
    }
    
    /** 
     * @param yearOfJoining
     */
    public void setYearOfJoining(short yearOfJoining) {
        this.yearOfJoining = yearOfJoining;
    }
    
    /** 
     * @param yearOfJoining
     */
    public void setYearOfJoining(String yearOfJoining) {
        this.yearOfJoining = Short.valueOf(yearOfJoining);
    }
    
    /** 
     * @return short
     */
    public short getMonthOfJoining() {
        return monthOfJoining;
    }
    
    /** 
     * @param monthOfJoining
     */
    public void setMonthOfJoining(short monthOfJoining) {
        this.monthOfJoining = monthOfJoining;
    }
    
    /** 
     * @param monthOfJoining
     */
    public void setMonthOfJoining(String monthOfJoining) {
        this.monthOfJoining = Short.valueOf(monthOfJoining);
    }
    
    /** 
     * @return String
     */
    public String getMonthOfJoiningName() {
        return this.monthOfJoiningName;
    }
    
    /** 
     * @param monthNameOfJoining
     */
    public void setMonthOfJoiningName(String monthOfJoiningName) {
        this.monthOfJoiningName = monthOfJoiningName;
    }
    
    /** 
     * @return String
     */
    public String getMonthOfJoiningShortname() {
        return this.monthOfJoiningShortname;
    }
    
    /** 
     * @param shortMonth
     */
    public void setMonthOfJoiningShortname(String monthOfJoiningShortname) {
        this.monthOfJoiningShortname = monthOfJoiningShortname;
    }
    
    /** 
     * @return short
     */
    public short getDayOfJoining() {
        return dayOfJoining;
    }
    
    /** 
     * @param dayOfJoining
     */
    public void setDayOfJoining(short dayOfJoining) {
        this.dayOfJoining = dayOfJoining;
    }
    
    /** 
     * @param dayOfJoining
     */
    public void setDayOfJoining(String dayOfJoining) {
        this.dayOfJoining = Short.valueOf(dayOfJoining);
    }
    
    /** 
     * @return String
     */
    public String getDowOfJoining() {
        return dowOfJoining;
    }
    
    /** 
     * @param dowOfJoining
     */
    public void setDowOfJoining(String dowOfJoining) {
        this.dowOfJoining = dowOfJoining;
    }
    
    /** 
     * @return String
     */
    public String getDowOfJoiningShortName() {
        return this.dowOfJoiningShortname;
    }
    
    /** 
     * @param shortDOW
     */
    public void setDowOfJoiningShortname(String dowOfJoiningShortname) {
        this.dowOfJoiningShortname = dowOfJoiningShortname;
    }
    
    /** 
     * @return double
     */
    public double getAgeInCompany() {
        return ageInCompany;
    }
    
    /** 
     * @param ageInCompany
     */
    public void setAgeInCompany(double ageInCompany) {
        this.ageInCompany = ageInCompany;
    }
    
    /** 
     * @param ageInCompany
     */
    public void setAgeInCompany(String ageInCompany) {
        this.ageInCompany = Double.valueOf(ageInCompany);
    }
    
    /** 
     * @return double
     */
    public double getSalary() {
        return salary;
    }
    
    /** 
     * @param salary
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    /** 
     * @param salary
     */
    public void setSalary(String salary) {
        this.salary = Double.valueOf(salary);
    }
    
    /** 
     * @return double
     */
    public double getLastPercentHike() {
        return this.lastHikePercent;
    }
    
    /** 
     * @param lastPercentHike
     */
    public void setLastHikePercent(double lastHikePercent) {
        this.lastHikePercent = lastHikePercent;
    }
    
    /** 
     * @param lastPercentHike
     */
    public void setLastHikePercent(String lastHikePercent) {
        var percentIndex = lastHikePercent.indexOf("%");
        if(percentIndex != -1) {
            lastHikePercent = lastHikePercent.substring(0, percentIndex);
        }
        this.lastHikePercent = Double.valueOf(lastHikePercent);
    }
    
    /** 
     * @return String
     */
    public String getSSN() {
        return this.ssn;
    }
    
    /** 
     * @param sSN
     */
    public void setSSN(String ssn) {
        this.ssn = ssn;
    }
    
    /** 
     * @return String
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    /** 
     * @param phoneNo
     */
    public void setPhoneNumber(String phoneNo) {
        this.phoneNumber = phoneNo;
    }
    
    /** 
     * @return String
     */
    public String getPlaceName() {
        return this.placeName;
    }
    
    /** 
     * @param placeName
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    
    /** 
     * @return String
     */
    public String getCounty() {
        return this.county;
    }
    
    /** 
     * @param county
     */
    public void setCounty(String county) {
        this.county = county;
    }
    
    /** 
     * @return String
     */
    public String getCity() {
        return this.city;
    }
    
    /** 
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /** 
     * @return String
     */
    public String getState() {
        return this.state;
    }
    
    /** 
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }
    
    /** 
     * @return String
     */
    public String getZip() {
        return this.zip;
    }
    
    /** 
     * @param zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    /** 
     * @return String
     */
    public String getRegion() {
        return region;
    }
    
    /** 
     * @param region
     */
    public void setRegion(String region) {
        this.region = region;
    }
    
    /** 
     * @return String
     */
    public String getUsername() {
        return this.username;
    }
    
    /** 
     * @param userName
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /** 
     * @return String
     */
    public String getPassword() {
        return password;
    }
    
    /** 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getSSN(), this.getEmailId(), this.getEmpId());
    }
    
    /** 
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Employee))
            return false;
        else {
            return  this.hashCode() == other.hashCode();
        }
    }
    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        Field[] fields = this.getClass().getDeclaredFields();
        try{
            for(Field f : fields) {
                var name = f.getName();
                var type = f.getType();
                switch (type.getSimpleName()) {
                    case "Character":
                    builder.add(name, f.getChar(this));
                    break;
                    case "Boolean":
                    builder.add(name, f.getBoolean(this));
                    break;
                    case "Byte":
                    builder.add(name, f.getByte(this));
                    break;
                    case "Date":
                    builder.add(name, ((Date)f.get(this)).toString());
                    break;
                    case "Double":
                    builder.add(name, f.getDouble(this));
                    break;
                    case "Float":
                    builder.add(name, f.getFloat(this));
                    break;
                    case "LocalDate":
                    builder.add(name, ((LocalDate)f.get(this)).toString());
                    break;
                    case "Short":
                    break;
                    case "Integer":
                    builder.add(name, f.getInt(this));
                    break;
                    case "Long":
                    builder.add(name, f.getLong(this));
                    break;
                    default:
                    builder.add(name, f.get(this).toString());
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build().toString(); 
    }
}
