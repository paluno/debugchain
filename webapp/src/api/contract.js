import getWeb3 from "./getWeb3"
import debugchainJson from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.abi';
import byteCode from '../../contracts/___contracts_contracts_DebugChain_sol_DebugChain.bin';

var appContract = {
  
  registerWeb3 () {
      getWeb3.then(result => {
        
      }).catch(e => {
        console.log('error in action registerWeb3', e)
      })
    },

  initContract: function() {
    let debugchainContract = null;
    appContract.registerWeb3();
    debugchainContract = web3.eth.contract(debugchainJson);    
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
                  if (typeof contract.address !== 'undefined') {
                        console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);

                        client.post("/projects/", {
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
                  else {
                    window.alert("The contract address is 'undefined'!");
                  }
                }
            );    

    return createdContract
  }
}
export default appContract