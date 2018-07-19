import Web3 from 'web3'
import Settings from '@/settings';
import ErrorContainer from "@/api/errorContainer";
import Localization from "@/api/errorLocalization";

let instance;

const getWeb3 = () => {
    if (typeof instance !== 'undefined') {
        return instance;
    }
    if (Settings.rpc.useHttpProviderUrl) {
        instance = new Web3(new Web3.providers.HttpProvider(Settings.rpc.useHttpProviderUrl));
        return instance;
    }
    if (typeof global.web3 !== 'undefined') {
        instance = new Web3(global.web3.currentProvider);
        return instance;
    }
    const e = new Error("Could not retrieve meta-mask provider");
    e.userMessage = Localization.getForContract(e);
    ErrorContainer.add(e);
};

export default getWeb3