package ru.sberbank.school.task03


import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.model.DocumentInfo

class Formatter implements ResponseFormatter{

    DocumentInfo document

    @Override
    String format(List<FxResponse> responses){

        document = Controller.buildDocument responses
        String output = Template.buildView document

        output
    }
}
