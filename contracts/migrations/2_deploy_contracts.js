var HelloWorld = artifacts.require("./HelloWorld.sol");
module.exports = function(deployer, helper, accounts) {
  return deployer.deploy(HelloWorld)
}
