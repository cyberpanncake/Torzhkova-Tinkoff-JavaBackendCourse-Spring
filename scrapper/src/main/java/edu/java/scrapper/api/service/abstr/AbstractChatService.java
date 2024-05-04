package edu.java.scrapper.api.service.abstr;

import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Subscription;
import edu.java.scrapper.api.domain.repository.ChatRepository;
import edu.java.scrapper.api.domain.repository.LinkRepository;
import edu.java.scrapper.api.domain.repository.SubscriptionRepository;
import edu.java.scrapper.api.exception.chat.ChatAlreadyRegisteredException;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.ScrapperService;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractChatService extends ScrapperService implements ChatService {

    public AbstractChatService(
        ChatRepository chatRepo, LinkRepository linkRepo,
        SubscriptionRepository subscriptionRepo
    ) {
        super(chatRepo, linkRepo, subscriptionRepo);
    }

    @Override
    public void register(long tgId) throws ChatAlreadyRegisteredException {
        chatRepo.findByTgId(tgId).ifPresent(ch -> {
            throw new ChatAlreadyRegisteredException();
        });
        chatRepo.add(tgId);
    }

    @Override
    public void unregister(long tgId) throws ChatNotFoundException {
        Chat chat = chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        List<Subscription> subscriptions = subscriptionRepo.findAllByChat(chat);
        for (Subscription subscription : subscriptions) {
            subscriptionRepo.remove(subscription);
            if (subscriptionRepo.linkNotFollowedByAnyone(subscription.linkId())) {
                linkRepo.remove(subscription.linkId());
            }
        }
        chatRepo.remove(tgId);
    }
}
