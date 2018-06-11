pragma solidity ^0.4.19;

contract DebugChain {

    struct Issue {
        uint id;
        mapping(address => uint) donations;
        uint donationSum;
        address developer;
        address[] reviewers;
        bool isApproved;
        bool isLocked;
        bool isDeveloped;
        mapping(address => bool) isReviewed;
        bool isCompleted;
        // to be able to check existence - solidity specialty
        bool exists;
    }

    uint projectId;
    address maintainer;
    // issues als mapping von GitLab-ID auf ein Issue-Struct
    mapping(uint => Issue) issues;
    mapping(address => uint) pendingWithdrawals;

    /**
     * Contract constructor.
     * Called upon contract deployment. Persists the maintainer address and
     * the projects GitLab-ID.
     *
     * @param _pId project ID to be persisted
     */
    function DebugChain(uint _pId) public {
        maintainer = msg.sender;
        projectId = _pId;
    }

    /**
     * Payable donation function. Takes the value-pair of message sender and
     * the added eth (wei) payload (msg.value) and adds it to the desired issue
     * dontaion mapping.
     * Only allows the donation to non-completed issues.
     *
     * @param _id issue ID to which the donation gets added
     */
    function donate(uint _id) payable public issueExists(_id) {
        // check if issue is completed.
        require(!issues[_id].isCompleted);

        issues[_id].donations[msg.sender] = msg.value;
        issues[_id].donationSum += msg.value;

        dontationRecieved(msg.sender, _id, msg.value);
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
     * @param a wallet address to be checked for pending funds
     */
    function getPendingWithdrawals(address a) public view returns (uint) {
        return pendingWithdrawals[a];
    }

    /**
     * Adds a new issue-object to the central issue-mapping. Uses the passed
     * GitLab-ID as a key to render lookups trivial.
     *
     * @param _id gitlab-issue-id to be used as map key
     */
    function createIssue(uint _id) public {
        // check that the issue does not already exist
        require(!issues[_id].exists);
        // create in-memory array and persist it to storage
        Issue memory issue = Issue({
            id: _id,
            donationSum: 0,
            developer: address(0),
            reviewers: new address[](0),
            isApproved: false,
            isLocked: false,
            isDeveloped: false,
            isCompleted: false,
            exists: true
            });
        issues[_id] = issue;
    }

    /**
     * Returns an issue split up in its individual fields since returning
     * structs is not possible in Solidity.
     *
     * @param _id issue to get
     */
    function getIssue(uint _id) public view issueExists(_id) returns
    (uint, uint, address[], bool) {
        // IN PROGRESS
        return (
        issues[_id].id,
        issues[_id].donationSum,
        issues[_id].reviewers,
        issues[_id].isCompleted
        );
    }

    // TODO: getIssues([])

    /**
     * Setter for an issues developer address.
     *
     * @param _id issue id
     * @param _developer developer address to set
     */
    function setDeveloper(uint _id, address _developer) public issueExists(_id) {
        issues[_id].developer = _developer;
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
     * Setter for an issues reviewer addresses.
     *
     * @param _id issue id
     * @param _reviewers reviewer addresses to set
     */
    function setReviewers(uint _id, address[] _reviewers) public issueExists(_id) {
        issues[_id].reviewers = _reviewers;
    }

    /**
     * Getter for an issues reviewer addresses.
     *
     * @param _id issue id
     */
    function getReviewers(uint _id) public view issueExists(_id) returns (address[]) {
        return issues[_id].reviewers;
    }

    function completeIssue(uint _id) public issueExists(_id) {
        // IN PROGRESS
        pendingWithdrawals[maintainer] = issues[_id].donationSum;
        issues[_id].isCompleted = true;

        issueCompleted(msg.sender, _id);
    }

    function deleteIssue(uint _id) public issueExists(_id) {

    }

    /**
     *
     *      EVENTS
     *
     **/

    event dontationRecieved (
        address indexed _from,
        uint indexed _id,
        uint _amount
    );

    event issueApproved (
        address indexed _by,
        uint indexed _id
    );

    event issueLocked (
        address indexed _by,
        uint indexed _id
    );

    event issueUnlocked (
        address indexed _by,
        uint indexed _id
    );

    event issueDeveloped (
        address indexed _by,
        uint indexed _id
    );

    event issueCompleted (
        address indexed _by,
        uint indexed _id
    );

    event issueReset (
        address indexed _by,
        address[] _reviewers,
        uint indexed _id
    );

    event issueDeleted (
        address indexed _by,
        uint indexed _id
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