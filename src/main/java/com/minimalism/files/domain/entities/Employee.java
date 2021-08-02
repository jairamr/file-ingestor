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

    public String getEmpID() {
        return empID;
    }
    public void setEmpID(String empID) {
        this.empID = empID;
    }
    public String getNamePrefix() {
        return namePrefix;
    }
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleInitial() {
        return middleInitial;
    }
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String geteMail() {
        return eMail;
    }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public String getFathersName() {
        return fathersName;
    }
    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }
    public String getMothersName() {
        return mothersName;
    }
    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }
    public String getMothersMaidenName() {
        return mothersMaidenName;
    }
    public void setMothersMaidenName(String mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getTimeOfBirth() {
        return timeOfBirth;
    }
    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }
    public double getAgeInYrs() {
        return ageInYrs;
    }
    public void setAgeInYrs(int ageInYrs) {
        this.ageInYrs = ageInYrs;
    }
    public void setAgeInYrs(String ageInYrs) {
        this.ageInYrs = Double.valueOf(ageInYrs);
    }
    public double getWeightInKgs() {
        return weightInKgs;
    }
    public void setWeightInKgs(double weightInKgs) {
        this.weightInKgs = weightInKgs;
    }
    public void setWeightInKgs(String weightInKgs) {
        this.weightInKgs = Double.valueOf(weightInKgs);
    }
    public String getDateOfJoining() {
        return dateOfJoining;
    }
    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }
    public String getQuarterOfJoining() {
        return quarterOfJoining;
    }
    public void setQuarterOfJoining(String quarterOfJoining) {
        this.quarterOfJoining = quarterOfJoining;
    }
    public String getHalfOfJoining() {
        return halfOfJoining;
    }
    public void setHalfOfJoining(String halfOfJoining) {
        this.halfOfJoining = halfOfJoining;
    }
    public short getYearOfJoining() {
        return yearOfJoining;
    }
    public void setYearOfJoining(short yearOfJoining) {
        this.yearOfJoining = yearOfJoining;
    }
    public void setYearOfJoining(String yearOfJoining) {
        this.yearOfJoining = Short.valueOf(yearOfJoining);
    }
    public short getMonthOfJoining() {
        return monthOfJoining;
    }
    public void setMonthOfJoining(short monthOfJoining) {
        this.monthOfJoining = monthOfJoining;
    }
    public void setMonthOfJoining(String monthOfJoining) {
        this.monthOfJoining = Short.valueOf(monthOfJoining);
    }
    public String getMonthNameOfJoining() {
        return monthNameOfJoining;
    }
    public void setMonthNameOfJoining(String monthNameOfJoining) {
        this.monthNameOfJoining = monthNameOfJoining;
    }
    public String getShortMonth() {
        return shortMonth;
    }
    public void setShortMonth(String shortMonth) {
        this.shortMonth = shortMonth;
    }
    public short getDayOfJoining() {
        return dayOfJoining;
    }
    public void setDayOfJoining(short dayOfJoining) {
        this.dayOfJoining = dayOfJoining;
    }
    public void setDayOfJoining(String dayOfJoining) {
        this.dayOfJoining = Short.valueOf(dayOfJoining);
    }
    public String getDowOfJoining() {
        return dowOfJoining;
    }
    public void setDowOfJoining(String dowOfJoining) {
        this.dowOfJoining = dowOfJoining;
    }
    public String getShortDOW() {
        return shortDOW;
    }
    public void setShortDOW(String shortDOW) {
        this.shortDOW = shortDOW;
    }
    public double getAgeInCompany() {
        return ageInCompany;
    }
    public void setAgeInCompany(double ageInCompany) {
        this.ageInCompany = ageInCompany;
    }
    public void setAgeInCompany(String ageInCompany) {
        this.ageInCompany = Double.valueOf(ageInCompany);
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public void setSalary(String salary) {
        this.salary = Double.valueOf(salary);
    }
    public double getLastPercentHike() {
        return lastPercentHike;
    }
    public void setLastPercentHike(double lastPercentHike) {
        this.lastPercentHike = lastPercentHike;
    }
    public void setLastPercentHike(String lastPercentHike) {
        var percentIndex = lastPercentHike.indexOf("%");
        if(percentIndex != -1) {
            lastPercentHike = lastPercentHike.substring(0, percentIndex);
        }
        this.lastPercentHike = Double.valueOf(lastPercentHike);
    }
    public String getSSN() {
        return SSN;
    }
    public void setSSN(String sSN) {
        SSN = sSN;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    public String getCounty() {
        return county;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.getSSN(), this.geteMail(), this.getEmpID());
    }
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Employee))
            return false;
        else {
            return  this.hashCode() == other.hashCode();
        }
    }
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
