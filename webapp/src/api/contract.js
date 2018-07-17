import getWeb3 from "./getWeb3"
import abi from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.abi';
import byteCode from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.bin';
import Localization from "@/api/errorLocalization";

const DEFAULT_GAS = 5000000;

// callback provider for ugly web3.js method signature
const handleCallback = (resolve, reject) => (error, result) => {
    if (error) {
        reject(error);
    }
    resolve(result);
};

const addUserMessageAndRethrow = (error) => {
    error.userMessage = Localization.getForContract(error);
    throw error;
}

export default class Contract {

    constructor(address) {
        this.web3 = getWeb3();
        this.instance = this.web3.eth.contract(abi);
        if (address) {
            this.instance = this.instance.at(address);
        }
    }

    deploy(projectId) {
        return new Promise((resolve, reject) => {
            const address = this.instance.address;
            if (address) {
                reject(new Error('Contract already deployed at ' + address));
            }
            let firstCall = true;
            this.instance.new(
                projectId,
                { data: byteCode, from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                (err, contract) => {
                    if (err) {
                        reject(err);
                    }
                    // callback is called twice, address is only present the second time
                    if (firstCall) {
                        firstCall = false;
                    } else {
                        this.instance = contract;
                        resolve(contract.address);
                    }
                }
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    donate(issueId, donationValue) {
        return new Promise((resolve, reject) => {
            this.instance.donate(
                issueId,
                { from: this.web3.eth.accounts[0], value: this.web3.toWei(donationValue, "ether"), gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    approve(issueId, reviewers) {
        return new Promise((resolve, reject) => {
            this.instance.setApproved(
                issueId,
                true, // TODO: this will be removed
                reviewers,
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    lock(issueId) {
        return new Promise((resolve, reject) => {
            this.instance.setDeveloper(
                issueId,
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    unlock(issueId) {
        return new Promise((resolve, reject) => {
            this.instance.unlockIssue(
                issueId,
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    develop(issueId) {
        return new Promise((resolve, reject) => {
            this.instance.setDeveloped(
                issueId,
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    review(issueId, accepted) {
        if (accepted === undefined) {
            accepted = true;
        }
        return new Promise((resolve, reject) => {
            this.instance.setReviewed(
                issueId,
                accepted,
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    withdraw() {
        return new Promise((resolve, reject) => {
            this.instance.withdraw(
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    reset(issueId) {
        return new Promise((resolve, reject) => {
            this.instance.resetIssue(
                issueId,
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }

    delete(issueId) {
        return new Promise((resolve, reject) => {
            this.instance.deleteIssue(
                issueId,
                { from: this.web3.eth.accounts[0], gas: DEFAULT_GAS },
                handleCallback(resolve, reject)
            );
        }).catch(error => addUserMessageAndRethrow(error));
    }
}
