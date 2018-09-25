import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import javax.servlet.http.HttpServletResponse;

public class InvalidInterfaceExternalConditionChecker implements ResponseFormatter {

    @Override
    public byte[] formatResponse(final WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) throws Exception {

        return null;
    }
}