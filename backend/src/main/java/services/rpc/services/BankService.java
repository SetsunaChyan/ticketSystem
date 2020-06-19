package services.rpc.services;

import javafx.util.Pair;

public interface BankService
{
    Pair<Boolean,String> pay(int money);
}
