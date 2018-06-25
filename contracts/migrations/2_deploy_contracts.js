var HelloWorld = artifacts.require("./HelloWorld.sol");
var DebugChain = artifacts.require("./DebugChain.sol");

module.exports = function(deployer, helper, accounts) {
  deployer.deploy(HelloWorld);
  deployer.deploy(DebugChain, 1);
}
