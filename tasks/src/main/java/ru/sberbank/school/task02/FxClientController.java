package ru.sberbank.school.task02;

import java.util.List;

import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

public interface FxClientController {

    List<FxResponse> fetchResult(List<FxRequest> requests);

    FxResponse fetchResult(FxRequest requests);
}
