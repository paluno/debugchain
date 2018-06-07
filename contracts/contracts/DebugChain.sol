pragma solidity ^0.4.0;

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
    constructor(uint _pId) public {
        maintainer = msg.sender;
        projectId = _pId;
    }

    /**
     * Payable donation function. Takes the value-pair of message sender and
     * the added eth (wei) payload (msg.value) and adds it to the desired issue
     * dontaion mapping.
     *
     * @param _id issue ID to which the donation gets added
     */
    function donate(uint _id) payable public {
        // check if issue is completed.
        require(issues[_id].isCompleted == false);

        issues[_id].donations[msg.sender] = msg.value;
        issues[_id].donationSum += msg.value;

        emit dontationRecieved(msg.sender, _id, msg.value);
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
        // create in-memory array and persist it to storage
        Issue memory issue = Issue({
            id: _id,
            donationSum: 0,
            developer: address(0),
            reviewers: new address[](0),
            isApproved: false,
            isLocked: false,
            isDeveloped: false,
            isCompleted: false
            });
        issues[_id] = issue;
    }

    function getIssue(uint _id) public view returns (uint, uint, bool) {
        // IN PROGRESS
        return (issues[_id].id, issues[_id].donationSum, issues[_id].isCompleted);
    }

    function completeIssue(uint _id) public {
        // IN PROGRESS
        pendingWithdrawals[maintainer] = issues[_id].donationSum;
        issues[_id].isCompleted = true;

        emit issueCompleted(msg.sender, _id);
    }

    function deleteIssue(uint _id) public {

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
     *      MODIFIER
     *
     **/

    /**
     * Set a condition to be met, otherwise an error will be thrown and
     * any funds connected to this transaction will be automatically
     * returned.
     */
    modifier onlyMaintainer() {
        require(msg.sender == maintainer);
        _;
    }

    modifier onlyDeveloper() {
        _;
    }

    modifier onlyReviewer() {
        _;
    }

}