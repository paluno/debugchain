pragma solidity ^0.4.0;

contract DebugChain {

    struct Issue {
        uint id;
        mapping(address => uint) donations;
        address developer;
        address[] reviewers;
        bool isApproved;
        bool isLocked;
        bool isDeveloped;
        mapping(address => bool) isReviewed;
        bool isClosed;
    }

    address maintainer;
    Issue[] issues;
    mapping(address => uint) pendingWithdrawals;

    /**
     * Contract constructor.
     */
    constructor() public {
        maintainer = msg.sender;
    }

    function donate(uint _id) public {

    }

    function withdraw() public {

    }

    function createIssue(uint _id) public {

    }

    function getIssue(uint _id) public {

    }

    function closeIssue(uint _id) public {

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
        uint indexed _iid,
        uint _amount
    );

    event issueApproved (
        address indexed _by,
        uint indexed _iid
    );

    event issueLocked (
        address indexed _by,
        uint indexed _iid
    );

    event issueUnlocked (
        address indexed _by,
        uint indexed _iid
    );

    event issueDeveloped (
        address indexed _by,
        uint indexed _iid
    );

    event issueClosed (
        address indexed _by,
        uint indexed _iid
    );

    event issueReset (
        address indexed _by,
        address[] _reviewers,
        uint indexed _iid
    );

    event issueDeleted (
        address indexed _by,
        uint indexed _iid
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