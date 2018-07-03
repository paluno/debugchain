import getWeb3 from "./getWeb3"
import abi from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.abi';
import byteCode from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.bin';

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
            this.instance.new(
                projectId,
                {data: byteCode, from: this.web3.eth.accounts[0]},
                (err, contract) => {
                    if (err) {
                        reject(err);
                    }
                    // callback is called twice, address is only present the second time
                    if (contract.address) {
                        this.instance = contract;
                        resolve(contract.address);
                    }
                }
            );
        })
    }
}