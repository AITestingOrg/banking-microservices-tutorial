package com.ultimatesoftware.banking.people.cmd.commands;

import com.ultimatesoftware.banking.people.cmd.models.AuthenticationDto;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class ResetPasswordCommand extends Command {
    private AuthenticationDto authenticationDto;

    public ResetPasswordCommand(AuthenticationDto authenticationDto) {
        super(new ObjectId(authenticationDto.getId()));
        this.authenticationDto = authenticationDto;
    }
}
