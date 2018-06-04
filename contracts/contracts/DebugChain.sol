pragma solidity ^0.4.0;

contract DebugChain {

    struct Donation{
        address donator;
        uint amount;
    }

    struct Issue {
        byte32 id;
        address developer;
        address [] reviewers;
        Donation [] donations;
        bool isApproved;
        bool isLocked;
        bool isDeveloped;
        bool isClosed;
        mapping(address => bool) isReviewed;
    }

    address maintainer;
    mapping(address => uint)pendingWithdrawals;
    Issue [] issues;

    //constructor
    function DebugChain() onlyMaintainer{

    }

    function isReviewer(address user) returns (bool success){
        for (uint i = 0; i < issues.length; i++){
            for (uint j = 0; j < reviewers.length; j++){
                if(user == issues[i].reviewers[j]){
                    return true;
                }
            }
        }
        return false;
    }

    function isDeveloper(address user) returns (bool success){
        for (uint i = 0; i < issues.length; i++){
            if(user == issues[i].developer){
                return true;
            }
        }
        return false;
    }
    // modifier
    modifier onlyReviewer{
        require(isReviewer(msg.sender));
        _;
    }

    modifier onlyDeveloper{
        require(isDeveloper(msg.sender));
        _;
    }

    modifier onlyMaintainer{
        require(msg.sender == maintainer);
        _;
    }

    function donate(byte32 id, uint amount) public{

        Donation donation;
        donation.donator = msg.sender;
        donation.amount  = amount;

        for (uint i = 0; i < issues.length; i++){
            if(id == issues[i].id){
                issues[i].donations.push(donation);
            }
        }

    }

}
