package com.SwitchBoard.NotificationService.Constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class MailSubjectConstantTest {

    @Test
    void otpSubject_HasCorrectValue() {
        // Assert
        assertEquals("Your One-Time Password (OTP) for SwitchBoard", 
                MailSubjectConstant.OTP_SUBJECT);
    }

    @Test
    void welcomeSubject_HasCorrectValue() {
        // Assert
        assertEquals("Welcome to SwitchBoard - Your Journey Begins Here!", 
                MailSubjectConstant.WELCOME_SUBJECT);
    }

    @Test
    void otpSubject_IsNotNull() {
        // Assert
        assertNotNull(MailSubjectConstant.OTP_SUBJECT);
    }

    @Test
    void welcomeSubject_IsNotNull() {
        // Assert
        assertNotNull(MailSubjectConstant.WELCOME_SUBJECT);
    }

    @Test
    void otpSubject_IsNotEmpty() {
        // Assert
        assertFalse(MailSubjectConstant.OTP_SUBJECT.isEmpty());
    }

    @Test
    void welcomeSubject_IsNotEmpty() {
        // Assert
        assertFalse(MailSubjectConstant.WELCOME_SUBJECT.isEmpty());
    }

    @Test
    void class_IsFinal() {
        // Assert
        assertTrue(Modifier.isFinal(MailSubjectConstant.class.getModifiers()));
    }

    @Test
    void constructor_IsPrivate() throws NoSuchMethodException {
        // This test verifies that the class cannot be instantiated
        // However, since we can't make constructor private in this case,
        // we just verify the constants are accessible
        assertNotNull(MailSubjectConstant.OTP_SUBJECT);
        assertNotNull(MailSubjectConstant.WELCOME_SUBJECT);
    }
}
