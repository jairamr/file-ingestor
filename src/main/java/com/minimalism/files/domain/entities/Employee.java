package com.minimalism.files.domain.entities;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class Employee {
    private String empID;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String gender;
    private String eMail;
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
    private String monthNameOfJoining;
    private String shortMonth;
    private short dayOfJoining;
    private String dowOfJoining;
    private String shortDOW;
    private double ageInCompany;
    private double salary;
    private double lastPercentHike;
    private String SSN;
    private String phoneNo;
    private String placeName;
    private String county;
    private String city;
    private String state;
    private String zip;
    private String region;
    private String userName;
    private String password;

    
    /** 
     * @param fieldSeparator
     * @param commaSeparated
     */
    public void makeFromFile(String fieldSeparator, String commaSeparated) {
        String[] fields = commaSeparated.split(fieldSeparator);
        this.setEmpID(fields[0]);
        this.setNamePrefix(fields[1]);
        this.setFirstName(fields[2]);
        this.setMiddleInitial(fields[3]);
        this.setLastName(fields[4]);
        this.setGender(fields[5]);
        this.seteMail(fields[6]);
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
        this.setMonthNameOfJoining(fields[19]);
        this.setShortMonth(fields[20]);
        this.setDayOfJoining(fields[21]);
        this.setDowOfJoining(fields[22]);
        this.setShortDOW(fields[23]);
        this.setAgeInCompany(fields[24]);
        this.setSalary(fields[25]);
        this.setLastPercentHike(fields[26]);
        this.setSSN(fields[27]);
        this.setPhoneNo(fields[28]);
        this.setPlaceName(fields[29]);
        this.setCounty(fields[30]);
        this.setCity(fields[31]);
        this.setState(fields[32]);
        this.setZip(fields[33]);
        this.setRegion(fields[34]);
        this.setUserName(fields[35]);
        this.setPassword(fields[36]);
    }

    public Employee(String fieldSeparator, String fromFile) {
        this.makeFromFile(fieldSeparator, fromFile);
    }

    
    /** 
     * @return String
     */
    public String getEmpID() {
        return empID;
    }
    
    /** 
     * @param empID
     */
    public void setEmpID(String empID) {
        this.empID = empID;
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
    public String geteMail() {
        return eMail;
    }
    
    /** 
     * @param eMail
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
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
    public String getMonthNameOfJoining() {
        return monthNameOfJoining;
    }
    
    /** 
     * @param monthNameOfJoining
     */
    public void setMonthNameOfJoining(String monthNameOfJoining) {
        this.monthNameOfJoining = monthNameOfJoining;
    }
    
    /** 
     * @return String
     */
    public String getShortMonth() {
        return shortMonth;
    }
    
    /** 
     * @param shortMonth
     */
    public void setShortMonth(String shortMonth) {
        this.shortMonth = shortMonth;
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
    public String getShortDOW() {
        return shortDOW;
    }
    
    /** 
     * @param shortDOW
     */
    public void setShortDOW(String shortDOW) {
        this.shortDOW = shortDOW;
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
        return lastPercentHike;
    }
    
    /** 
     * @param lastPercentHike
     */
    public void setLastPercentHike(double lastPercentHike) {
        this.lastPercentHike = lastPercentHike;
    }
    
    /** 
     * @param lastPercentHike
     */
    public void setLastPercentHike(String lastPercentHike) {
        var percentIndex = lastPercentHike.indexOf("%");
        if(percentIndex != -1) {
            lastPercentHike = lastPercentHike.substring(0, percentIndex);
        }
        this.lastPercentHike = Double.valueOf(lastPercentHike);
    }
    
    /** 
     * @return String
     */
    public String getSSN() {
        return SSN;
    }
    
    /** 
     * @param sSN
     */
    public void setSSN(String sSN) {
        SSN = sSN;
    }
    
    /** 
     * @return String
     */
    public String getPhoneNo() {
        return phoneNo;
    }
    
    /** 
     * @param phoneNo
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    
    /** 
     * @return String
     */
    public String getPlaceName() {
        return placeName;
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
        return county;
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
        return city;
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
        return state;
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
        return zip;
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
    public String getUserName() {
        return userName;
    }
    
    /** 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
        return Objects.hash(this.getSSN(), this.geteMail(), this.getEmpID());
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
