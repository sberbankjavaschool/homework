package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

import java.util.List;

public class Client implements FxClientController {
    @Override
    public List<FxResponse> fetchResult(List<FxRequest> requests) {
        return null;
    }

    @Override
    public FxResponse fetchResult(FxRequest requests) {
        return null;
    }
}
