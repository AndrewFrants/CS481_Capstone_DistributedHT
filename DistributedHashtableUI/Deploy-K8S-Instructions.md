# Kubernetes application based on tutorial

# Prepared by Andrew Frantsuzov for Distributed Service Capstone

1. Create RG
az group create --name DHTService --location westus

2. Create AKS cluser

az aks create --resource-group DHTService --name DhtAksCluster --node-count 2 --enable-addons monitoring --generate-ssh-keys

3. Connect to cluster

az aks install-cli

4. Get Credentials

az aks get-credentials --resource-group DHTService --name DhtAksCluster

5. Get Nodes

kubectl get nodes

6. Create YAML file

azure-vote.yaml