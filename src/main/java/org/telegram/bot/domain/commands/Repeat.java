package org.telegram.bot.domain.commands;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.bot.Bot;
import org.telegram.bot.Parser;
import org.telegram.bot.domain.CommandParent;
import org.telegram.bot.domain.TextAnalyzer;
import org.telegram.bot.domain.entities.Chat;
import org.telegram.bot.domain.entities.CommandProperties;
import org.telegram.bot.domain.entities.LastCommand;
import org.telegram.bot.domain.entities.User;
import org.telegram.bot.services.ChatService;
import org.telegram.bot.services.LastCommandService;
import org.telegram.bot.services.UserService;
import org.telegram.bot.services.UserStatsService;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class Repeat implements TextAnalyzer {

    private final ApplicationContext context;
    private final ChatService chatService;
    private final UserService userService;
    private final UserStatsService userStatsService;
    private final LastCommandService lastCommandService;

    @Override
    public void analyze(Bot bot, Update update) {
        Message message = update.getMessage();
        String textMessage = message.getText();

        if (textMessage != null && textMessage.startsWith(".")) {
            Chat chat = chatService.get(message.getChatId());
            User user = userService.get(message.getFrom().getId());
            LastCommand lastCommand = lastCommandService.get(chat);

            if (lastCommand != null) {
                CommandProperties commandProperties = lastCommand.getCommandProperties();

                if (userService.isUserHaveAccessForCommand(userService.getCurrentAccessLevel(user.getUserId(), chat.getChatId()).getValue(), commandProperties.getAccessLevel())) {
                    update.getMessage().setText(lastCommand.getCommandProperties().getCommandName());
                    userStatsService.incrementUserStatsCommands(chatService.get(chat.getChatId()), userService.get(user.getUserId()));

                    Parser parser = new Parser(bot, (CommandParent<?>) context.getBean(commandProperties.getClassName()), update);
                    parser.start();
                }
            }
        }
    }
}