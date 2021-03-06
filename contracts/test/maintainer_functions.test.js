var DebugChain = artifacts.require("DebugChain");

function assertError(error) {
    const ganacheEnv = error.message.search('status 0') >= 0;
    const testrpcEnv = error.message.search('VM Exception') >= 0;
    assert(ganacheEnv || testrpcEnv, 'expected an error, but none was thrown');
}

contract('DebugChain Maintainer Functions Test', async (accounts) => {
    it("should not reset the issue by non-maintainers", async () => {
        let instance = await DebugChain.deployed();
        await instance.donate(1, { value: web3.toWei(1, "ether"), from: accounts[0] });
        try {
            await instance.resetIssue(1, { from: accounts[1] });
            assert.fail('should have thrown before');
        } catch (error) {
            assertError(error);
        }
    });
    it("should reset the issue. Its lifecycleStatus is: DEVELOPED and a succesful review has been set", async () => {
        let instance = await DebugChain.deployed();
        await instance.setApproved(1, true, [accounts[1], accounts[2]], { from: accounts[0] });
        await instance.setDeveloper(1, { from: accounts[3] });
        await instance.setDeveloped(1, { from: accounts[3] });
        await instance.setReviewed(1, true, { from: accounts[1] });
        let issue = await instance.getIssue.call(1);
        assert.equal(issue[3][0], accounts[1], 'reviewer should be set');
        assert.isTrue(issue[4][0], 'review status should be true');
        await instance.resetIssue(1, { from: accounts[0] });
        let _issue = await instance.getIssue.call(1);
        assert.notEqual(_issue[3][0], accounts[1], 'reviewer should not be set');
        assert.isNotTrue(_issue[4][0], 'review status should be false');
        assert.equal(_issue[5], 0, 'lifecycle status should be: DEFAULT')
    });
});