package due.debugchain.persistence;

import due.debugchain.persistence.entities.ReviewerEntity;
import due.debugchain.persistence.repositories.ReviewerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;

/**
 * Service for handling business logic regarding reviewers.
 */
@Service
@RequiredArgsConstructor
public class ReviewerService {

    private final ReviewerRepository reviewerRepository;

    /**
     * Creates a new persistent reviewer by mapping their GitLab ID with their Ethereum address.
     *
     * @param gitlabId reviewer's GitLab ID
     * @param address  reviewer's Ethereum address
     * @return persisted reviewer
     */
    public ReviewerEntity createReviewer(Long gitlabId, Address address) {
        ReviewerEntity reviewer = new ReviewerEntity();
        reviewer.setGitlabId(gitlabId);
        reviewer.setAddress(address);
        return reviewerRepository.save(reviewer);
    }
}
