<template>
    <div class="FAQ">
        <Navigation />
        <div class="content">
            <h2><strong>Frequently asked questions</strong></h2>
            <br>

            <p><strong>How do I install Metamask?</strong> <br>
            You can download Metamask from its official website <a href="https://metamask.io/">here</a>.
            </p>

            <p><strong>How do I connect to the custom test network?</strong> <br>
            To start working with DebugChain and familiarize yourself with the system without using actual Ether on the main Ethereum Network,
            simply chose "Custom RPC" in Metamask and connect to the following local RPC within our backend: <font color="blue"> http://debugchain.k202.de:9654 </font>
            </p>
            
            <p><strong>Where do I register for DebugChain?</strong> <br>
            You can use DebugChain by simply logging in with your GitLab account.
                If you do not have a GitLab account yet, you can visit its official website <a href="https://about.gitlab.com/">here</a> and sign up.
            </p>
            
            <p><strong>Why are there 2 status fields for each issue? What is the difference between the GitLab status and the DebugChain status?</strong> <br>
            After creating a respective DebugChain project for a given GitLab project and donating some Ether to it, the issue gets tracked in the smart contract on the Ethereum blockchain.
                From now on, the issue's lifecycle on the blockchain is independent from the GitLab lifecycle and locking, developing or closing the respective issue in the DebugChain system does not have any direct consequences for the issue status in GitLab.
            </p>

            <p><strong>What is the address I am supposed to enter in my profile page and what is it used for?</strong> <br>
            You need to deposit your public address of your Ethereum wallet. This address is then used for the interactions with the Ethereum Smart Contract and cannot be changed afterwards.
            </p>

            <p><strong>What do I need to pay attention to when interacting with DebugChain via Metamask?</strong> <br>
            Always make sure you have selected the correct account in Metamask, you actually want to send the transactions with with regards to the DebugChain system (most probably the accounts whose address you have deposited in your profile page).
            Otherwise, you might get some Errors when trying to interact with the Smart Contract going through an issue's lifecycle.
            </p>

            <p><strong>Why can't I finish the development of my issue?</strong> <br>
            Check your Metamask address/account. The one you finish the development with needs to be the same you initially locked the issue with!
            </p>

            <p><strong>What does the lifecycle of an issue look like?</strong> <br>
            The following diagram depicts an issue's lifecycle: <br>
            <img src="../../public/issue_lifecycle.png" alt="issue lifecycle"/> </p>
        </div>
    </div>
</template>

<script>
import Navigation from "@/components/Navigation";

export default {
  name: "faq",
  components: {
      Navigation
  }
};

</script>
