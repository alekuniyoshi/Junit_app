package org.akuniyoshi.junit5app.examples.models;

import org.akuniyoshi.junit5app.examples.exceptions.InsufficientBalanceException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class AccountTest {
    Account account;

    @BeforeEach
    void initMethodTest() {
        System.out.println("Starting the test method..");
        this.account = new Account("Alejandro", new BigDecimal("1000.12345"));
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finishing the test method...");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Starting the test..");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finishing the test...");
    }

    @Nested
    @DisplayName("Testing the things about the Accounts")
    class AccountNameBalance {

        @Test
        void testAccountName() {
            account.setPerson("Alejandro");
            String valid = "Alejandro";
            String real = account.getPerson();
            assertNotNull(real);
            assertEquals(valid, real);
            assertTrue(real.equals(valid));
        }


        @Test
        void testAccount() {
            assertEquals(1000.12345, account.getAmount().doubleValue());
            assertNotNull(account.getAmount());
            assertFalse(account.getAmount().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(account.getAmount().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        void testAccountByReference() {
            Account account2 = new Account("Alejandro", new BigDecimal("1000.12345"));
            assertEquals(account, account2);

        }

        @Test
        void testAccountDebit() {
            account.debit(new BigDecimal(100));
            assertNotNull(account.getAmount());
            assertEquals(900, account.getAmount().intValue());
            assertEquals("900.12345", account.getAmount().toPlainString());
        }

        @Test
        void testAccountCredit() {
            account.credit(new BigDecimal(100));
            assertNotNull(account.getAmount());
            assertEquals(1100, account.getAmount().intValue());
            assertEquals("1100.12345", account.getAmount().toPlainString());
        }

        @Test
        void testInsufficientBalance() {
            Exception exception = assertThrows(InsufficientBalanceException.class, () -> account.debit(new BigDecimal(1500)));

            String actual = exception.getMessage();
            String expected = "Insufficient Balance";
            assertEquals(expected, actual);
        }

        @Test
        @Disabled
        @DisplayName("Working on new functionality")
        void testTransferMoneyToAccount() {
            Account account1 = new Account("Martin", new BigDecimal(2000));
            Account account2 = new Account("Alejandro", new BigDecimal(1000));

            Bank bank = new Bank();
            bank.setName("World Bank");
            bank.transfer(account1, account2, new BigDecimal(500));
            assertEquals("1500", account1.getAmount().toPlainString());
            assertEquals("1500", account2.getAmount().toPlainString());


        }

        @RepeatedTest(value = 3, name = "Repiticion number {currentRepetition} of {totalRepetitions}")
        @DisplayName("Testing all the things :)")
        void testRelationBankAccountRepetition(RepetitionInfo info) {

            if (info.getCurrentRepetition() == 2) {
                System.out.println("we are in the repetittion number " + info.getCurrentRepetition());
            }
            Account account1 = new Account("Martin", new BigDecimal(2000));
            Account account2 = new Account("Alejandro", new BigDecimal(1000));

            Bank bank = new Bank();
            bank.addAccount(account1);
            bank.addAccount(account2);
            bank.setName("World Bank");
            bank.transfer(account1, account2, new BigDecimal(500));

            assertAll(() -> assertEquals("1500", account1.getAmount().toPlainString(), () -> "Expected 1500, but come " + account1.getAmount().toPlainString()),
                    () -> assertEquals("1500", account2.getAmount().toPlainString()),
                    () -> assertEquals(2, bank.getAccounts().size()),
                    () -> assertEquals("World Bank", account1.getBank().getName()),
                    () -> assertEquals("Alejandro", bank.getAccounts().stream()
                            .filter(a -> a.getPerson().equals("Alejandro"))
                            .findFirst().get().getPerson()),
                    () -> assertTrue(bank.getAccounts().stream().anyMatch(a -> a.getPerson().equals("Martin"))));

        }

        @Test
        void testAccountDev() {
            boolean isDev = "dev".equals(System.getProperty("ENVIRONMENT"));
//        assumeTrue(isDev);
            assumingThat(isDev, () -> {
                assertEquals(1000.123112222222145, account.getAmount().doubleValue());
                assertNotNull(account.getAmount());
            });

            assertFalse(account.getAmount().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(account.getAmount().compareTo(BigDecimal.ZERO) > 0);
        }

    }


    @Nested
    class SOTest {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testLinuxOrMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }

        @Test
        @DisabledOnOs(OS.LINUX)
        void testNoLinux() {
        }
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testJR8() {
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void printSystemProperties() {
            Properties pro = System.getProperties();
            pro.forEach((k, v) -> System.out.println(k + ":" + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "14.0.2")
        void testJavaVersion() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testArch64() {

        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDevEnvironment() {
        }


        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-14.0.2.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "12")
        void testNumbersOfProcessors() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProd() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "test")
        void testEnvTest() {
        }


        @Test
        void printEnvironmentVariable() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + ":" + v));
        }
    }


    @ParameterizedTest(name = "number {index} executing with the value {0} - {argumentsWithNames}")
    @ValueSource(strings = {"100", "200", "300"})
    void testAccountCredit(String amount) {
        account.credit(new BigDecimal(amount));
        assertNotNull(account.getAmount());
//        assertEquals(1100, account.getAmount().intValue());
//        assertEquals("1100.12345", account.getAmount().toPlainString());
        assertTrue(account.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

}