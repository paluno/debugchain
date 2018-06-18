import getWeb3 from "getWeb3"
import debugchainJson from '../../abi/___contracts_contracts_DebugChain_sol_DebugChain';
//const debugchainJson from '../../abi/___contracts_contracts_DebugChain_sol_DebugChain.json';

var appContract = {
  registerWeb3 () {
      console.log('registerWeb3 Action being executed')
      getWeb3.then(result => {
        console.log('web3 is register')
      }).catch(e => {
        console.log('error in action registerWeb3', e)
      })
    },

  initContract: function() {
    var debugchainContract = null;
    console.log("initContract");
    console.log("create debugchain Contract:");
    console.log(debugchainJson);
    appContract.registerWeb3();
    debugchainContract = web3.eth.contract(debugchainJson);
    console.log(debugchainContract);
    return debugchainContract;
  }
export default appContract