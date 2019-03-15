package com.ultimatesoftware.banking.account.cmd.commands;

import org.bson.types.ObjectId;

public interface ICommand {
    ObjectId getId();
}
