pragma solidity ^0.4.23;

contract DebugChain {

    struct Issue {
        uint id;
        mapping(address => uint) donations;
        address[] donationsLookup;
        uint donationSum;
        address developer;
        address[] reviewers;
        /*
            0: default
            1: approved
            2: locked
            3: developed
            4: completed
        */
        uint lifecycleStatus;
        mapping(address => bool) isReviewed;
        // to be able to check an issues existence
        bool exists;
    }

    uint public projectId;
    address public maintainer;
    // issues as a mapping of a GitLab-ID to an issue struct
    mapping(uint => Issue) public issues;
    uint[] public issuesLookup;
    mapping(address => uint) public pendingWithdrawals;

    /**
     * Contract constructor.
     * Called upon contract deployment. Persists the maintainer address and
     * the projects GitLab-ID.
     *
     * @param _pId project ID to be persisted
     */
    constructor(uint _pId) public {
        maintainer = msg.sender;
        projectId = _pId;
    }

    /**
     * Returns some basic contract/project related information.
     */
    function info() public view returns (uint, address) {
        return (projectId, maintainer);
    }

    /**
     * Payable donation function. Takes the value-pair of message sender and
     * the added eth (wei) payload (msg.value) and adds it to the desired issue
     * donation mapping.
     * Only allows the donation to non-completed issues.
     * If an issue isn't registered at the point of donation, it is created and
     * the donation value is added.
     *
     * @param _id issue ID to which the donation gets added
     */
    function donate(uint _id) payable public {
        // Only allow non-empty donations
        require(msg.value != 0);

        // check if issue exists - if not create it
        if (!issues[_id].exists) {
            createIssue(_id);
            // and add the issue id to the global lookup table
            issuesLookup.push(_id);
        } else {
            // if an issue exists, check if the issue is completed
            require(issues[_id].lifecycleStatus != 4);
        }
        // if it exists and is not completed, append the donation value
        issues[_id].donations[msg.sender] += msg.value;
        issues[_id].donationSum += msg.value;
        // add the donator to the lookup array
        if (!isRegisteredDonator(_id, msg.sender)) {
            issues[_id].donationsLookup.push(msg.sender);
        }
        // emit corresponding event
        emit DonationReceived(msg.sender, _id, msg.value);
    }

    /**
     * Checks if the given address is already registered as a donator for the
     * given issue.
     *
     * @param _id issue id
     * @param _donator the donators wallet address
     */
    function isRegisteredDonator(uint _id, address _donator) private view returns (bool) {
        for (uint i = 0; i < issues[_id].donationsLookup.length; i++) {
            if (issues[_id].donationsLookup[i] == _donator) {
                return true;
            }
        }
        return false;
    }

    /**
     * Withdrawal function to the internal contract 'wallet'. A user can
     * withdraw any accumulated funds using one transaction.
     * To prevent re-entrance attacks the pending amount gets set to 0.
     */
    function withdraw() public {
        uint amount = pendingWithdrawals[msg.sender];
        pendingWithdrawals[msg.sender] = 0;
        msg.sender.transfer(amount);
    }

    /**
     * Returns the pending withdrawal amount in wei for a given user address.
     *
     * @param _add wallet address to be checked for pending funds
     */
    function getPendingWithdrawals(address _add) public view returns (uint) {
        return pendingWithdrawals[_add];
    }

    /**
     * Returns the pending withdrawal amount in wei for a given array of user
     * addresses.
     *
     * @param _adds array of wallet addresses
     */
    function getPendingWithdrawals(address[] _adds) public view returns (uint[]) {
        uint[] memory result;
        for (uint i = 0; i < _adds.length; i++) {
            result[i] = (pendingWithdrawals[_adds[i]]);
        }
        return result;
    }

    /**
     * Adds a new issue-object to the central issue-mapping. Uses the passed
     * GitLab-ID as a key to render lookups trivial.
     *
     * @param _id gitlab issue id to be used as map key
     */
    function createIssue(uint _id) private {
        // create new in-memory struct and persist it to storage
        Issue memory issue = Issue({
            id: _id,
            donationsLookup: new address[](0),
            donationSum: 0,
            developer: address(0),
            reviewers: new address[](0),
            lifecycleStatus: 0,
            exists: true
            });
        issues[_id] = issue;
    }

    /**
     * Returns an issue split up in its individual fields as an array since
     * returning structs is not supported by solidity.
     *
     * @param _id issue to get
     */
    function getIssue(uint _i) public view returns (
    // TODO look into possible complexity flag system
        uint _id,
        uint _donationSum,
        address _developer,
        address[] _reviewers,
        bool[] _reviewStatus,
        uint _lifecycleStatus,
        address[] _donators,
        uint[] _donationValues
    ) {
        _id = _i;
        _donationSum = issues[_i].donationSum;
        _developer = issues[_i].developer;
        _reviewers = issues[_i].reviewers;
        _reviewStatus = getReviewStatus(_i);
        _lifecycleStatus = issues[_i].lifecycleStatus;
        _donators = issues[_i].donationsLookup;
        _donationValues = getDonationValues(issues[_i].id);
    }

    /**
     * Returns the array of an issues donation values per donator.
     *
     * @param _id issue id
     */
    function getDonationValues(uint _id) private view returns(uint[]) {
        uint[] memory values = new uint[](issues[_id].donationsLookup.length);
        // for each donator(-address) add the corresponding donation amount
        for (uint i = 0; i < issues[_id].donationsLookup.length; i++) {
            values[i] = issues[_id].donations[issues[_id].donationsLookup[i]];
        }
        return values;
    }

    /**
     * Setter for an issues developer address.
     *
     * @param _id issue id
     */
    function setDeveloper(uint _id) public issueExists(_id) {
        issues[_id].developer = msg.sender;
        // lock the issue after a developer is assigned
        setLocked(_id);
    }

    /**
     * Getter for an issues developer address.
     *
     * @param _id issue id
     */
    function getDeveloper(uint _id) public view issueExists(_id) returns (address) {
        return issues[_id].developer;
    }

    /**
     * Unlocks an issue. Resets the developer address and resets the lifecycle status
     * to approved.
     *
     * @param _id issue id
     */
    function unlockIssue(uint _id) public issueExists(_id) {
        // only allow the maintainer and current developer to unlock an issue
        require(msg.sender == maintainer || msg.sender == issues[_id].developer);
        // TODO test
        // reset developer address
        issues[_id].developer = address(0);
        // reset lifecycle to allow re-locking
        issues[_id].lifecycleStatus = 1;
        // fire lifecycle event
        emit IssueUnlocked(msg.sender, _id);
    }

    /**
     * Setter for an issues reviewer addresses.
     *
     * @param _id issue id
     * @param _reviewers reviewer addresses to set
     */
    function setReviewers(uint _id, address[] _reviewers) public issueExists(_id) onlyMaintainer {
        require(_reviewers.length > 0);

        // set the new reviewers
        issues[_id].reviewers = _reviewers;
        // set all the review stati to false
        for (uint i = 0; i < issues[_id].reviewers.length; i++) {
            issues[_id].isReviewed[_reviewers[i]] = false;
        }
    }

    /**
     * Getter for an issues reviewer addresses.
     *
     * @param _id issue id
     */
    function getReviewers(uint _id) public view issueExists(_id) returns (address[]) {
        return issues[_id].reviewers;
    }

    /**
     * Returns the review status for each of the registered reviewers as a
     * boolean array.
     *
     * @param _id issue id
     */
    function getReviewStatus(uint _id) public view issueExists(_id) returns (bool[]) {
        bool[] memory _status = new bool[](issues[_id].reviewers.length);
        for (uint i = 0; i < issues[_id].reviewers.length; i++) {
            // return the boolean review status for each reqistered reviewer
            _status[i] = issues[_id].isReviewed[issues[_id].reviewers[i]];
        }
        return _status;
    }

    /**
     * Setter for an issues approved status.
     *
     * @param _id issue id
     * @param _val approved status to set
     */
    function setApproved(uint _id, bool _val, address[] _reviewers) public issueExists(_id) onlyMaintainer {
        require(_reviewers.length > 0);
        // TODO: check if issue is in later status (like completed)
        if (_val) {
            issues[_id].lifecycleStatus = 1;
        } else {
            // TODO possible to unapprove?
            issues[_id].lifecycleStatus = 0;
        }
        // set the reviewers
        setReviewers(_id, _reviewers);
        // fire lifecycle event
        emit IssueApproved(msg.sender, _id);
    }

    /**
     * Setter for an issues locked status.
     *
     * @param _id issue id
     */
    function setLocked(uint _id) private issueExists(_id) {
        // require approved status and no set developer
        require(issues[_id].lifecycleStatus == 1);

        issues[_id].lifecycleStatus = 2;
        // fire lifecycle event
        emit IssueLocked(msg.sender, _id);
    }

    /**
     * Setter for an issues developed status.
     *
     * @param _id issue id
     */
    function setDeveloped(uint _id) public issueExists(_id) onlyDeveloper(_id) {
        // only accept developed flag, when issue is locked
        require(issues[_id].lifecycleStatus == 2);

        issues[_id].lifecycleStatus = 3;
        // fire lifecycle event
        emit IssueDeveloped(msg.sender, _id);
    }

    /**
     * Setter for an issues reviewed status.
     *
     * @param _id issue id
     * @param _val reviewed status to set
     */
    function setReviewed(uint _id, bool _val) public issueExists(_id) onlyReviewer(_id) {
        // only accept reviews, when issue is marked as developed
        require(issues[_id].lifecycleStatus == 3);
        // TODO lock reviewer out after succesful review

        // on successful review, write new value to chain and check for completion
        if (_val) {
            issues[_id].isReviewed[msg.sender] = _val;
            // check if all reviewers approved the developed changes
            if (checkForCompletion(_id)) {
                completeIssue(_id);
            }
        } else { // on a rejection-review, reset review status
            resetReviewStatus(_id);
            // and reset lifecycle to in development
            issues[_id].lifecycleStatus = 2;
        }

        // fire lifecycle event
        emit IssueReviewed(msg.sender, _id);
    }

    /**
     * Check if all reviewers approved the developed changes to trigger the
     * issues completion.
     *
     * @param _id issue id
     */
    function checkForCompletion(uint _id) private view returns (bool) {
        bool[] memory status = getReviewStatus(_id);
        for (uint i = 0; i < status.length; i++) {
            if (!status[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Mark an issue as completes and release its donated funds to the assigned
     * developer.
     *
     * @param _id issue id
     */
    function completeIssue(uint _id) private issueExists(_id) {
        pendingWithdrawals[issues[_id].developer] = issues[_id].donationSum;
        issues[_id].lifecycleStatus = 4;
        // fire lifecycle event
        emit IssueCompleted(msg.sender, _id);
    }

    /**
     * Resets an issues. reviewers and its corresponding review status.
     *
     * @param _id issue id
     */
    function resetIssue(uint _id) public issueExists(_id) onlyMaintainer {
        // reset the review status
        resetReviewStatus(_id);
        // reset the reviewers address array
        issues[_id].reviewers = new address[](0);
        // reset the developer
        unlockIssue(_id);
        // set lifecycle to default, TODO should be removed in final version
        issues[_id].lifecycleStatus = 0;

        // fire lifecycle event
        emit IssueReset(msg.sender, _id);
    }

    function resetReviewStatus(uint _id) private {
        for (uint i = 0; i < issues[_id].reviewers.length; i++) {
            // return the boolean review status for each reqistered reviewer
            // TODO delete in place of false?
            issues[_id].isReviewed[issues[_id].reviewers[i]] = false;
        }
    }

    /**
     * Deletes an issue and releases all funds back to the donators.
     * Only accessible to the project maintainer.
     *
     * @param _id issue id
     */
    function deleteIssue(uint _id) public issueExists(_id) onlyMaintainer {
        // release funds to withdrawal mapping for each donator
        releaseDonatedFunds(_id);
        // clear mappings prior to removal to ensure complete removal
        clearIssueMappings(_id);
        // delete issue from lookup array and mapping
        removeFromIssueLookup(_id);
        delete issues[_id];
        // fire lifecycle event
        emit IssueDeleted(msg.sender, _id);
    }

    /**
     * Clear an issues internal mappings to ensure a full deletion of that
     * issue.
     * The 'delete' keyword does not traverse into mappings of structs, which
     * means that the data is kept by the contract. To enable potential
     * node-self-issued cleanup and prevent errors on re-approving an issue,
     * mappings are cleared prior to the final deletion.
     *
     * @param _id issue id
     */
    function clearIssueMappings(uint _id) private {
        // clear donation mapping
        for (uint i = 0; i < issues[_id].donationsLookup.length; i++) {
            delete issues[_id].donations[issues[_id].donationsLookup[i]];
        }
        // clear review status
        for (uint j = 0; j < issues[_id].reviewers.length; j++) {
            delete issues[_id].isReviewed[issues[_id].reviewers[j]];
        }
    }


    /**
     * Removes an entry from the lookup array - cleanly - based on the given id.
     * Since the order of the keys in the lookup array does not matter, an
     * efficient algorithm was chosen.
     *
     * @param _id issue id
     */
    function removeFromIssueLookup(uint _id) private {
        uint index;
        // find the issue to be deleted
        for (uint i = 0; i < issuesLookup.length; i++) {
            if (issuesLookup[i] == _id) {
                index = i;
            }
        }
        // take the last element in the lookup table and replace value to be deleted
        issuesLookup[index] = issuesLookup[issuesLookup.length-1];
        // shorten the array by one, de facto removing the now duplicate element
        issuesLookup.length--;
    }

    /**
     * Returns the issue lookup table, which contains all the keys that
     * are present in the issues hashmap.
     */
    function getIssueLookup() public view returns (uint[]) {
        return issuesLookup;
    }

    /**
     * Releases an issues donated funds back to the original donators.
     *
     * @param _id issue id
     */
    function releaseDonatedFunds(uint _id) private {
        // go over each of the issues donators and credit their donated value back
        for (uint i = 0; i < issues[_id].donationsLookup.length; i++) {
            pendingWithdrawals[issues[_id].donationsLookup[i]] = issues[_id].donations[issues[_id].donationsLookup[i]];
        }
    }

    /**
     *
     *      EVENTS
     *
     **/

    event DonationReceived (
        address indexed _from,
        uint indexed _id,
        uint _amount
    );

    event IssueApproved (
        address indexed _by,
        uint indexed _id
    );

    event IssueLocked (
        address indexed _by,
        uint indexed _id
    );

    event IssueUnlocked (
        address indexed _by,
        uint indexed _id
    );

    event IssueDeveloped (
        address _by,
        uint _id
    );

    event IssueReviewed (
        address _by,
        uint _id
    );

    event IssueCompleted (
        address _by,
        uint _id
    );

    event IssueReset (
        address _by,
        uint _id
    );

    event IssueDeleted (
        address _by,
        uint _id
    );

    /**
     *
     *      MODIFIERS
     *
     **/

    /**
     * Modifier to check if an issue exists.
     *
     * @param _id issue id to check
     */
    modifier issueExists(uint _id) {
        require(issues[_id].exists);
        _;
    }

    /**
     * Modifier to check if the message sender is the maintainer.
     */
    modifier onlyMaintainer() {
        require(msg.sender == maintainer);
        _;
    }

    /**
     * Modifier to check if the message sender is an issues assigned developer.
     *
     * @param _id issue id
     */
    modifier onlyDeveloper(uint _id) {
        require(msg.sender == issues[_id].developer);
        _;
    }

    /**
     * Modifier to check if the message sender is one of an issues assigned
     * reviewers.
     *
     * @param _id issue id
     */
    modifier onlyReviewer(uint _id) {
        bool isReviewer = false;
        for (uint i = 0; i < issues[_id].reviewers.length; i++) {
            if (msg.sender == issues[_id].reviewers[i]) {
                isReviewer = true;
            }
        }
        require(isReviewer);
        _;
    }

}