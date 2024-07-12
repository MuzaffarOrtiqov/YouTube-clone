package uz.urinov.youtube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.subscription.SubscriptionCreateDTO;
import uz.urinov.youtube.dto.subscription.SubscriptionDTO;
import uz.urinov.youtube.entity.SubscriptionEntity;
import uz.urinov.youtube.enums.NotificationType;
import uz.urinov.youtube.enums.SubscriptionStatus;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.SubscriptionRepository;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subcriptionRepository;
    @Autowired
    private ChannelService channelService;

    public String create(SubscriptionCreateDTO dto) {
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setChannelId(dto.getChannelId());
        subscriptionEntity.setNotificationType(dto.getNotificationType());
        subscriptionEntity.setProfileId(SecurityUtil.getProfileId());
        subcriptionRepository.save(subscriptionEntity);
        return "Channel created with id : " + subscriptionEntity.getId();
    }

    public SubscriptionDTO updateStatus(String channelId, SubscriptionStatus status) {
        SubscriptionEntity subscriptionEntity = getSubscription(channelId);
        subscriptionEntity.setStatus(status);
        subcriptionRepository.updateStatus(channelId, status);
        SubscriptionDTO subscriptionDTO = toDTO(subscriptionEntity);
        subscriptionDTO.setStatus(status);
        return subscriptionDTO;
    }

    public SubscriptionDTO toDTO(SubscriptionEntity subscriptionEntity) {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(subscriptionEntity.getId());
        subscriptionDTO.setChannelId(subscriptionEntity.getChannelId());
        subscriptionDTO.setNotificationType(subscriptionEntity.getNotificationType());
        return subscriptionDTO;
    }

    public SubscriptionEntity getSubscription(String channelId) {
        Integer profileId = SecurityUtil.getProfileId();
        Optional<SubscriptionEntity> subscriptionEntity = subcriptionRepository.findSubscriptionForUser(channelId, profileId);
        if (subscriptionEntity.isEmpty()) {
            throw new AppBadException("The user not subscribed to this channel");
        }
        return subscriptionEntity.get();
    }

    public SubscriptionDTO updateNotificationType(String channelId, NotificationType notificationType) {
        SubscriptionEntity subscriptionEntity = getSubscription(channelId);
        subscriptionEntity.setNotificationType(notificationType);
        subcriptionRepository.updateNotoficationType(channelId, notificationType);
        return toDTO(subscriptionEntity);
    }

    public SubscriptionDTO subscriptionInfo(SubscriptionEntity subscriptionEntity) {
        // id,channel(id,name,photo(id,url)),notification_type
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(subscriptionEntity.getId());
        subscriptionDTO.setChannelDTO(channelService.getChannelDTOByChannelId(subscriptionEntity.getChannelId()));
        subscriptionDTO.setNotificationType(subscriptionEntity.getNotificationType());
        return subscriptionDTO;
    }

    public List<SubscriptionDTO> getAllSubscriptions() {
        Integer profileId = SecurityUtil.getProfileId();
        List<SubscriptionEntity> subscriptionEntityList = subcriptionRepository.findAllSubscriptions(profileId);
        List<SubscriptionDTO> subscriptionDTOList;
        if (subscriptionEntityList.isEmpty()) {
            throw new AppBadException("No subscriptions found");
        }
        subscriptionDTOList = subscriptionEntityList
                .stream()
                .map(subscriptionEntity -> {
                    return subscriptionInfo(subscriptionEntity);
                })
                .toList();

        return subscriptionDTOList;
    }

    public List<SubscriptionDTO> getAllSubscriptionsForAdmin() {
        List<SubscriptionEntity> subscriptionEntityList = (List<SubscriptionEntity>) subcriptionRepository.findAll();
        List<SubscriptionDTO> subscriptionDTOList;
        if (subscriptionEntityList.isEmpty()) {
            throw new AppBadException("No subscriptions found");
        }
        subscriptionDTOList = subscriptionEntityList
                .stream()
                .map(subscriptionEntity -> {
                    SubscriptionDTO subscriptionDTO = subscriptionInfo(subscriptionEntity);
                    subscriptionDTO.setCreatedDate(subscriptionEntity.getCreatedDate());
                    return subscriptionDTO;
                })
                .toList();

        return subscriptionDTOList;
    }
}
