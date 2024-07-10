package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.subscription.SubscriptionCreateDTO;
import uz.urinov.youtube.dto.subscription.SubscriptionDTO;
import uz.urinov.youtube.enums.NotificationType;
import uz.urinov.youtube.enums.SubscriptionStatus;
import uz.urinov.youtube.service.SubscriptionService;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;

@RequestMapping("/subscription")
@RestController
@Slf4j
@Tag(name = "API s for subscription")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;
    @PostMapping("/create")
    @Operation(summary = "Create subscription for a user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> createSubscription(@RequestBody SubscriptionCreateDTO dto) {
        log.info("Subscription created for channel id :{}",dto.getChannelId());
        return ResponseEntity.ok(subscriptionService.create(dto));
    }
    @PutMapping("/update-status/{channelId}")
    @Operation(summary = "change channel status ")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<SubscriptionDTO> changeStatus(@PathVariable String channelId,
                                                        @RequestParam(name = "status") @NotNull SubscriptionStatus status) {
        log.info("Subscription status is being changed for channel id :{}",channelId);
        return ResponseEntity.ok(subscriptionService.updateStatus(channelId,status));
    }
    @PutMapping("/update-notification-type/{channelId}")
    @Operation(summary = "change channel notification type ")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<SubscriptionDTO> changeNotification(@PathVariable String channelId,
                                                        @RequestParam(name = "notification-type") @NotNull NotificationType notificationType) {
        log.info("Subscription notification type is being changed for channel id :{}",channelId);
        return ResponseEntity.ok(subscriptionService.updateNotificationType(channelId,notificationType));
    }

    @GetMapping("/subscription-list")
    @Operation(summary = "Get all subscription list ")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        log.info("User {} getting all subscriptions ", SecurityUtil.getProfileId());
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/subscription-list-admin")
    @Operation(summary = "Get all subscription list ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptionsForAdmin() {
        log.info("User {} getting all subscriptions ", SecurityUtil.getProfileId());
        return ResponseEntity.ok(subscriptionService.getAllSubscriptionsForAdmin());
    }


}
