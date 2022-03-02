package com.minimalism.files.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.minimalism.shared.common.AllEnums.DataTypes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputFieldTests {
    @Test
    void testEquals_for_same_instance() {
        InputField iut = new InputField();
        iut.setName("EmpId");
        iut.setDataType(DataTypes.STRING);
        iut.setMaximumLength((short)10);
        iut.setMaximumLength((short)6);
        iut.setNullable(false);
        iut.setPosition((short)0);
        iut.setValue("20362");
        InputField other = iut;
        assertEquals(iut, other);
    }
    @Test
    void testEquals_for_same_values() {
        InputField iut1 = new InputField();
        iut1.setName("EmpId");
        iut1.setDataType(DataTypes.STRING);
        iut1.setMaximumLength((short)10);
        iut1.setMaximumLength((short)6);
        iut1.setNullable(false);
        iut1.setPosition((short)0);
        iut1.setValue("20362");

        InputField iut2 = new InputField();
        iut2.setName("EmpId");
        iut2.setDataType(DataTypes.STRING);
        iut2.setMaximumLength((short)10);
        iut2.setMaximumLength((short)6);
        iut2.setNullable(false);
        iut2.setPosition((short)0);
        iut2.setValue("20362");
        
        assertEquals(iut1, iut2);
    }

    @Test
    void textTimeFormat_am_pm() {
        String time = "11:32:56 PM";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:m:s a", Locale.US);
        LocalTime lt = LocalTime.parse(time, formatter);
        assertEquals(23, lt.getHour());
        
    }
    @Test
    void testSetValue_boolean_true() {
        InputField iut = new InputField();
        iut.setDataType(DataTypes.BOOLEAN);
        iut.setValue(Boolean.valueOf(true));
        assertEquals(Boolean.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(Boolean.valueOf(true), iut.getValue());
    }
    @Test
    void testSetValue_boolean_false() {
        InputField iut = new InputField();
        iut.setDataType(DataTypes.BOOLEAN);
        iut.setValue(Boolean.valueOf(false));
        assertEquals(Boolean.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(Boolean.valueOf(false), iut.getValue());
    }
    @Test
    void testSetValue_boolean_invalid_value_throws_exception() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)5);
        iut.setDataType(DataTypes.BOOLEAN);
        assertThrows(NumberFormatException.class, () -> {    
            iut.setValue("t");
            //assertEquals(Boolean.TRUE, iut.isValid());
            //assertEquals(Boolean.valueOf(true), iut.getValue());
        });
    }
    @Test
    void testSetValue_currency_input_as_string() {
        InputField iut = new InputField();
        iut.setDataType(DataTypes.BIG_DECIMAL);
        iut.setValue("200.83");
        assertEquals(BigDecimal.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_currency_input_as_big_decimal() {
        InputField iut = new InputField();
        iut.setDataType(DataTypes.BIG_DECIMAL);
        iut.setValue(new BigDecimal("200.83"));
        assertEquals(BigDecimal.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_currency_input_as_double() {
        InputField iut = new InputField();
        iut.setDataType(DataTypes.BIG_DECIMAL);
        iut.setValue(200.83d);
        assertEquals(BigDecimal.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_currency_input_as_float() {
        InputField iut = new InputField();
        iut.setDataType(DataTypes.BIG_DECIMAL);
        iut.setValue(200.83f);
        assertEquals(BigDecimal.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(200.83, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testSetValue_currency_invalid_value_throws_exception() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)7);
        iut.setMaximumLength((short)12);
        iut.setDataType(DataTypes.BIG_DECIMAL);
        assertThrows(NumberFormatException.class, () -> {
            iut.setValue("currency");
        });
    }
    @ParameterizedTest
    @ValueSource(strings = {"2020-12-14", "2020-01-01"})
    void testSetValue_local_date_default_ISO_format_input_as_string(String date) {
        InputField iut = new InputField();
        iut.setDataType(DataTypes.LOCAL_DATE);
        iut.setValue(date);
        assertEquals(LocalDate.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(date, iut.getValue().toString());
    }
    @ParameterizedTest
    @ValueSource(strings = {"2020/12/14", "2020/01/01"})
    void testSetValue_local_date_slash_format_input_as_string(String date) {
        List<String> dateFormats =new ArrayList<>();
        dateFormats.add("yyyy/MM/dd");
        List<String> timeFormats =new ArrayList<>();
        timeFormats.add("HH:mm:ss");
        
        InputField iut = new InputField(dateFormats, timeFormats);
        iut.setDataType(DataTypes.LOCAL_DATE);
        iut.setValue(date);
        assertEquals(LocalDate.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(date, ((LocalDate)iut.getValue()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    }
    @ParameterizedTest
    @ValueSource(strings = {"2020 12 14", "2020 01 01"})
    void testSetValue_local_date_space_format_input_as_string(String date) {
        List<String> dateFormats =new ArrayList<>();
        dateFormats.add("yyyy MM dd");
        List<String> timeFormats =new ArrayList<>();
        timeFormats.add("HH:mm:ss");
        
        InputField iut = new InputField(dateFormats, timeFormats);
        iut.setDataType(DataTypes.LOCAL_DATE);
        iut.setValue(date);
        assertEquals(LocalDate.class.getSimpleName(), iut.getDataType().getTypeName());
        assertEquals(date, ((LocalDate)iut.getValue()).format(DateTimeFormatter.ofPattern("yyyy MM dd")));
    }
    @Test
    void testSetValue_string_notNullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.STRING);
        iut.setValue("Test String");
        assertEquals("Test String", iut.getValue().toString());
    }
    @Test
    void testSetValue_string_notNullable_null_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.STRING);
        assertThrows(NullPointerException.class, () -> {
            iut.setValue(null);
        });
    }
    @Test
    void testSetValue_string_nullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.STRING);
        iut.setValue("Test String");
        assertEquals("Test String", iut.getValue().toString());
    }
    @Test
    void testSetValue_string_nullable_isNull() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.STRING);
        iut.setValue(null);
        assertEquals(null, iut.getValue());
    }
    @Test
    void testSetValue_float_notNullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.FLOAT);
        iut.setValue(23.456f);
        assertEquals(23.456f, (float)iut.getValue());
    }
    @Test
    void testSetValue_float_notNullable_null_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.FLOAT);
        assertThrows(NullPointerException.class, () -> {
            iut.setValue(null);
        });
    }
    @Test
    void testSetValue_float_nullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.FLOAT);
        iut.setValue(23.456f);
        assertEquals(23.456f, (float)iut.getValue());
    }
    @Test
    void testSetValue_float_nullable_isNull() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.FLOAT);
        iut.setValue(null);
        assertEquals(null, iut.getValue());
    }
    @Test
    void testSetValue_double_notNullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.DOUBLE);
        iut.setValue(99997.33d);
        assertEquals(99997.33d, (double)iut.getValue());
    }
    @Test
    void testSetValue_double_notNullable_nan_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.DOUBLE);
        assertThrows(NumberFormatException.class, () -> {
            iut.setValue("abcd");
        });
    }
    @Test
    void testSetValue_double_notNullable_null_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.DOUBLE);
        assertThrows(NullPointerException.class, () -> {
            iut.setValue(null);
        });
    }
    @Test
    void testSetValue_double_nullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.DOUBLE);
        iut.setValue(88889.445d);
        assertEquals(88889.445d, (double)iut.getValue());
    }
    @Test
    void testSetValue_double_nullable_nan_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.DOUBLE);
        assertThrows(NumberFormatException.class, () -> {
            iut.setValue("abcd");
        });
    }
    @Test
    void testSetValue_double_nullable_isNull() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.DOUBLE);
        iut.setValue(null);
        assertEquals(null, iut.getValue());
    }
    @Test
    void testSetValue_integer_notNullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.INTEGER);
        iut.setValue(99997);
        assertEquals(99997, (int)iut.getValue());
    }
    @Test
    void testSetValue_integer_notNullable_decimal_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.INTEGER);
        assertThrows(NumberFormatException.class, () -> {
            iut.setValue(8897.33d);
        });
    }
    @Test
    void testSetValue_integer_notNullable_null_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.INTEGER);
        assertThrows(NullPointerException.class, () -> {
            iut.setValue(null);
        });
    }
    @Test
    void testSetValue_integer_nullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.INTEGER);
        iut.setValue(88889);
        assertEquals(88889, (int)iut.getValue());
    }
    @Test
    void testSetValue_integer_nullable_decimal_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.INTEGER);
        assertThrows(NumberFormatException.class, () -> {
            iut.setValue(88889.33d);
        });
    }
    @Test
    void testSetValue_integer_nullable_isNull() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.INTEGER);
        iut.setValue(null);
        assertEquals(null, iut.getValue());
    }
    @Test
    void testSetValue_long_notNullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.LONG);
        iut.setValue(99997);
        assertEquals(99997, (long)iut.getValue());
    }
    @Test
    void testSetValue_long_notNullable_decimal_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.LONG);
        assertThrows(NumberFormatException.class, () -> {
            iut.setValue(8897.33d);
        });
    }
    @Test
    void testSetValue_long_notNullable_null_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.LONG);
        assertThrows(NullPointerException.class, () -> {
            iut.setValue(null);
        });
    }
    @Test
    void testSetValue_long_nullable_has_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.LONG);
        iut.setValue(Long.MAX_VALUE);
        assertEquals(9223372036854775807L, (long)iut.getValue());
    }
    @Test
    void testSetValue_long_nullable_decimal_value() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.LONG);
        assertThrows(NumberFormatException.class, () -> {
            iut.setValue(88889.33d);
        });
    }
    @Test
    void testSetValue_long_nullable_isNull() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)11);
        iut.setDataType(DataTypes.LONG);
        iut.setValue(null);
        assertEquals(null, iut.getValue());
    }
    @Test
    void testIsValid_boolean_trueValue_notNullable() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)5);
        iut.setDataType(DataTypes.BOOLEAN);
        iut.setValue(Boolean.valueOf(Boolean.TRUE));
        assertEquals(Boolean.TRUE, iut.isValid());
        assertEquals(Boolean.valueOf(true), iut.getValue());
    }
    @Test
    void testIsValid_boolean_falseValue_notNullable() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)5);
        iut.setDataType(DataTypes.BOOLEAN);
        iut.setValue(Boolean.valueOf(Boolean.FALSE));
        assertEquals(Boolean.TRUE, iut.isValid());
        assertEquals(Boolean.valueOf(false), iut.getValue());
    }
    @Test
    void testIsValid_boolean_trueValue_nullable() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)5);
        iut.setDataType(DataTypes.BOOLEAN);
        iut.setValue(Boolean.valueOf(Boolean.TRUE));
        assertEquals(Boolean.TRUE, iut.isValid());
        assertEquals(Boolean.valueOf(true), iut.getValue());
    }
    @Test
    void testIsValid_boolean_value_nullable_isNull() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.TRUE);
        iut.setMinimumLength((short)4);
        iut.setMaximumLength((short)5);
        iut.setDataType(DataTypes.BOOLEAN);
        iut.setValue(null);
        assertEquals(Boolean.TRUE, iut.isValid());
        assertEquals(null, iut.getValue());
    }
    @Test
    void testIsValid_currency_notNullable_valid() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)7);
        iut.setMaximumLength((short)12);
        iut.setDataType(DataTypes.BIG_DECIMAL);
        iut.setValue(1234.5678d);
        assertEquals(Boolean.TRUE, iut.isValid());
        assertEquals(1234.5678d, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testIsValid_currency_notNullable_min_length_fails() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)7);
        iut.setMaximumLength((short)12);
        iut.setDataType(DataTypes.BIG_DECIMAL);
        iut.setValue(12.345d);
        assertEquals(Boolean.FALSE, iut.isValid());
        assertEquals(12.345d, ((BigDecimal)iut.getValue()).doubleValue());
    }
    @Test
    void testIsValid_currency_notNullable_max_length_fails() {
        InputField iut = new InputField();
        iut.setNullable(Boolean.FALSE);
        iut.setMinimumLength((short)7);
        iut.setMaximumLength((short)12);
        iut.setDataType(DataTypes.BIG_DECIMAL);
        iut.setValue(BigDecimal.valueOf(1234567.8901234d));
        assertEquals(Boolean.FALSE, iut.isValid());
        assertEquals(1234567.8901234d, ((BigDecimal)iut.getValue()).doubleValue());
    }
}
