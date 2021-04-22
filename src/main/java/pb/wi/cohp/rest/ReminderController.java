package pb.wi.cohp.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.reminder.Reminder;
import pb.wi.cohp.domain.reminder.ReminderService;

import javax.validation.Valid;

@RestController
@RequestMapping("/reminders")
@Validated
public class ReminderController {

    final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{idTest}")
    public ResponseEntity<?> createReminder(
            @Valid @RequestBody Reminder reminder,
            @PathVariable Long idTest
    ){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return ResponseEntity.ok(
                reminderService.createReminder(
                        reminder.getDate(),
                        reminder.getTime(),
                        reminder.getNote(),
                        reminder.isEmailReminder(),
                        reminder.isSmsReminder(),
                        username,
                        idTest
                )
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping()
    public ResponseEntity<?> editReminder(
            @Valid @RequestBody Reminder reminder){
        return ResponseEntity.ok(
                reminderService.editReminder(
                        reminder.getDate(),
                        reminder.getTime(),
                        reminder.getNote(),
                        reminder.isEmailReminder(),
                        reminder.isSmsReminder(),
                        reminder.getId()
                )
        );
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{idReminder}")
    public void removeReminder(
            @PathVariable Long idReminder){
        reminderService.deleteReminder(idReminder);
    }


    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping("/{username}")
    public ResponseEntity<?> getReminders(@PathVariable String username){
        return ResponseEntity.ok(reminderService.getReminders(username));
    }

    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping("/{username}/{idReminder}")
    public ResponseEntity<?> getReminder(
            @PathVariable Long idReminder,
            @PathVariable String username){
        return ResponseEntity.ok(
                reminderService.getReminder(idReminder)
        );
    }
}
