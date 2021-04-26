package pb.wi.cohp.domain.reminder

import org.junit.experimental.categories.Category
import org.springframework.core.env.Environment
import pb.wi.cohp.config.error.exception.InvalidDataException
import pb.wi.cohp.config.error.exception.ObjectNotFoundException
import pb.wi.cohp.domain.test.Test
import pb.wi.cohp.domain.test.TestService
import pb.wi.cohp.domain.user.User
import pb.wi.cohp.domain.user.UserService
import pb.wi.cohp.typeOfTest.UnitTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

@Category(UnitTest.class)

class ReminderServiceSpec extends Specification{
    Environment environment

    ReminderRepository reminderRepository

    UserService userService

    TestService testService

    ReminderService reminderService

    def setup(){
        environment = Stub(Environment)
        reminderRepository = Stub(ReminderRepository)
        userService = Stub(UserService)
        testService = Stub(TestService)
        reminderService = new ReminderService(environment, reminderRepository, userService, testService)
    }

    def id = 1L
    def testUsername = "testUsername"

    def "Should return true when date is current date and time is after current time"(){
        given:"Defined data to call method"
        def date = LocalDate.now()
        def addMinutes = 10
        def time = LocalTime.now().plusMinutes(addMinutes)
        when:"Check if date and time is valid"
        def result = reminderService.validDateAndTime(date, time)
        then:
        result
    }

    def "Should return false when date is current date and time is before current time"(){
        given:"Defined data to call method"
        def date = LocalDate.now()
        def minusMinutes = 10
        def time = LocalTime.now().minusMinutes(minusMinutes)
        when:"Check if date and time is valid"
        def result = reminderService.validDateAndTime(date, time)
        then:
        !result
    }

    def "Should return false when date and time is before current date and time"(){
        given:"Defined data to call method"
        def minusDays = 10
        def date = LocalDate.now().minusDays(minusDays)
        def time = LocalTime.now()
        when:"Check if date and time is valid"
        def result = reminderService.validDateAndTime(date, time)
        then:
        !result
    }

    def "Should throw an ObjectNotFoundException exception when trying to find a reminder with the given id that does not exist"(){
        given:"Defined data to call method"
        reminderRepository.findById(id) >> Optional.empty()
        when:"Try get reminder"
        reminderService.getReminder(id)
        then:
        thrown(ObjectNotFoundException)
    }

    def "Should return reminder by id"(){
        given:"Defined data to call method"
        def plusDays = 10
        def reminder = new Reminder()
        reminder.setDate(LocalDate.now().plusDays(plusDays))
        reminder.setTime(LocalTime.now())
        and:"findById method return optional reminder"
        reminderRepository.findById(id) >> Optional.of(reminder)
        when:"Try get reminder"
        def result = reminderService.getReminder(id)
        then:
        reminder.equals(result)
    }

    def "Should return created reminder"(){
        given:"Defined data to call method"
        def plusDays = 10
        def date = LocalDate.now().plusDays(plusDays)
        def time = LocalTime.now()
        def reminder = new Reminder()
        reminder.setDate(date)
        reminder.setTime(time)
        def user = new User()
        def test = new Test()
        and:"getUserByUsername return user, findTestById method return test, save method return reminder"
        userService.getUserByUsername(_ as String) >> user
        testService.findTestById(id) >> test
        reminderRepository.save(_ as Reminder) >> reminder
        when:"Try create reminder"
        def result = reminderService.createReminder(date, time, "", false, false, testUsername, id)
        then:
        result.equals(reminder)
    }

    def "Should thrown InvalidDataException when try create reminder with date and time before current date and time"(){
        given:"Defined data to call method"
        def minusDays = 10
        def date = LocalDate.now().minusDays(minusDays)
        def time = LocalTime.now()
        def reminder = new Reminder()
        reminder.setDate(date)
        reminder.setTime(time)
        def user = new User()
        def test = new Test()
        and:"getUserByUsername return user, findTestById return test"
        userService.getUserByUsername(_ as String) >> user
        testService.findTestById(id) >> test
        when:"Try create reminder"
        reminderService.createReminder(date, time, "", false, false, testUsername, id)
        then:
        thrown(InvalidDataException)
    }

    def "Should return edited reminder"(){
        given:"Defined data to call method"
        def plusDays = 10
        def date = LocalDate.now().plusDays(plusDays)
        def time = LocalTime.now()
        def reminder = new Reminder()
        reminder.setDate(date)
        reminder.setTime(time)
        and:"findById method return optional reminder, save method return reminder"
        reminderRepository.findById(id) >> Optional.of(reminder)
        reminderRepository.save(_ as Reminder) >> reminder
        when:"Try edit reminder"
        def result = reminderService.editReminder(date, time, "", false, false, id)
        then:
        result.equals(reminder)
    }

    def "Should thrown InvalidDataException when try edit reminder with date and time before current date and time"(){
        given:"Defined data to call method"
        def minusDays = 10
        def date = LocalDate.now().minusDays(minusDays)
        def time = LocalTime.now()
        def reminder = new Reminder()
        reminder.setDate(date)
        reminder.setTime(time)
        when:"Try edit reminder"
        reminderService.editReminder(date, time, "", false, false, id)
        then:
        thrown(InvalidDataException)
    }

}
