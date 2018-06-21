import getWeb3 from "./getWeb3"
import debugchainJson from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.abi';
import byteCode from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.bin';

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
    let debugchainContract = null;
    console.log("initContract");
    console.log("create debugchain Contract:");

    console.log(debugchainJson);
    appContract.registerWeb3();
    debugchainContract = web3.eth.contract(debugchainJson);
    console.log(debugchainContract);
    
    return debugchainContract;
  },

  newContract: function(id, client, self){
    //init contract
    let debugchainContract = appContract.initContract();
    let createdContract = null;
    createdContract =  debugchainContract.new(
              id,
              {
                  from: web3.eth.accounts[0],
                  data: byteCode,
                  gas: '4700000'
              }, function (e, contract){
                  console.log(e, contract);
                  if (typeof contract.address !== 'undefined') {
                        console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
                        
                        client.post("/projects/", {
                          //TODO: address has to be set accordingly from result of appContract.newContract - call
                            address: contract.address,
                            gitlabId: id
                          })
                          .then(function(response) {
                            console.log("Project created");
                            self.$router.push({
                                name: "issueList",
                                params: { projectId: id.toString() }
                              });
                          });
                  
                  }
                }
            );
    console.log("createdContract "+createdContract);
    

    return createdContract
  }
}
export default appContract