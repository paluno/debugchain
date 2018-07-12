package due.debugchain.chain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Issue was not Found.")
public class IssueNotFoundException extends RuntimeException {

    public IssueNotFoundException() {
        super("Issue was not found.");
    }
}
