package ru.sberbank.school.task02;

import ru.sberbank.school.task02.util.FxRequest;
import ru.sberbank.school.task02.util.FxResponse;

import java.util.List;

public interface FxClientController {

    List<FxResponse> fetchResult(List<FxRequest> requests);

    FxResponse fetchResult(FxRequest requests);
}
