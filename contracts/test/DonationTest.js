var DebugChain = artifacts.require("DebugChain");

contract('DebugChain', async (accounts) => {
    it("should create an new issue and donate 5 ether", async () => {
        let instance = await DebugChain.deployed();
        await instance.donate(1, {value: web3.toWei(5,"ether"), from: accounts[0]});
        let issue = await instance.getIssue.call(1);
        console.log(issue);
        assert.equal(web3.fromWei(issue[1]), 5);
    });
    it("should set the donator address", async () => {
        let instance = await DebugChain.deployed();
        let issue = await instance.getIssue.call(1);
        assert.equal(issue[9][0], accounts[0]);
    });
    it("should assign the donator address the donation value", async () => {
        let instance = await DebugChain.deployed();
        let issue = await instance.getIssue.call(1);
        assert.equal(web3.fromWei(issue[10][0]).toNumber(), 5);
    });
    it("should add the issue to the issue lookup table ", async () => {
        let instance = await DebugChain.deployed();
        let issues = await instance.getIssueLookup.call();
        assert.equal(issues[0], 1);
    });
});