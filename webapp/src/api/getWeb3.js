import Web3 from 'web3'

/*
* 1. Check for injected web3 (mist/metamask)
* 2. If metamask/mist create a new web3 instance and pass on result
* 3. Get networkId - Now we can check the user is connected to the right network to use our dApp
* 4. Get user account from metamask
* 5. Get user balance
*/

let getWeb3 = new Promise(function (resolve, reject) {
  // Check for injected web3 (mist/metamask)
  //var web3js = window.web3
  //if (typeof web3js !== 'undefined') {
    //var web3 = new Web3(web3js.currentProvider)
    var web3 = new Web3(new Web3.providers.HttpProvider('http://localhost:7545'))
    console.log("web3 instance")
    console.log(web3)
    resolve({
      injectedWeb3: web3.isConnected(),
      web3 () {
        return web3
      }
    })
  //} else {
    // web3 = new Web3(new Web3.providers.HttpProvider('http://localhost:7545')) GANACHE FALLBACK
    //reject(new Error('Unable to connect to Metamask'))
  //}
})

export default getWeb3