# Deploy in Kubernetes Cluster

## Prerequisites
- A Kubernetes cluster running Kubernetes 1.13.0 or later
- Kubectl utility
- Helm
- Supported version of Docker Container Engine

## Install Metallb
In case of owning a Kubernetes cluster in the cloud, skip this installation. (The cloud provider will provision for you, a cloud native load balancer automatically)

Metallb is of an open-source L2 Load Balancer, perfect for bare metal implementations. Metallb will be used to expose a single entry point of the Kubernetes data plane, to your home LAN.    
    
For the installation we can get the template from the metallb helm repo, and store it locally. ***Or*** you can find the template `metallb-0.14.3.yml` for metallb v0.14.3 in the `metallb` folder.
```
helm repo add metallb https://metallb.github.com/metallb
```
Find a compatible version with your Kubernetes version.
```
helm search repo metallb metallb --versions
```
Get the metallb manifest template from helm repo.
```
CHART_VERSION="0.14.3"
APP_VERSION="0.14.3"

helm template metallb metallb \
    --repo https://metallb.github.com/metallb \
    --version ${CHART_VERSION} \
    --namespace metallb-system > \
    metallb-${APP_VERSION}.yml
```

Create `metallb-system` namepace and apply the manifest.
```
kubectl create namespace metallb-system
kubectl apply -f metallb-${APP_VERSION}.yml
``` 
Create an `IPAddressPool` resource from where `LoadBalancer` type services will have assigned an external IP address from a specified range.    

In our case the only LoadBlancer service, will be the `Nginx Ingress Gateway`.   
The nginx container will act as reverse proxy to the internal `ClusterIP` type services, that will route the HTTP traffic based on specific rules.    

The `address-pool.yml` is provided in the `metallb` folder. Change the IP addresses range accordingly. 

```
kubectl apply -f ./metallb/address-pool.yml -n metallb-system
```

Finally, apply the `l2-advertisement.yml` provided in the `metallb folder`, in order for metallb to apply BGP routing and advertiesment procedures.

```
kubectl apply -f ./metallb/l2-advertisement.yml -n metallb-system
```

## Install Nginx Ingress Gateway
Same for the nginx installation applies for nginx. You can use the manifest template `nginx-1.7.1.yml` provided in the `nginx` folder, or you can get it with helm.    

First, you have to find a compatible `Ingress-Nginx version` with your k8s supported version.   
You can find it at, https://github.com/kubernetes/ingress-nginx

Then get the manifest template from the helm chart, and store it locally.
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx

# Find the Chart version that matches the Ingress-Nginx version
helm search repo ingress-nginx --versions

CHART_VERSION="4.6.1"
APP_VERSION="1.7.1"

helm template ingress-nginx ingress-nginx \
    --repo https://kubernetes.github.io/ingress-nginx \
    --version ${CHART_VERSION} --namespace ingress-nginx > \
    nginx-ingress.${APP_VERSION}.yml
```  

Finally, create the `ingress-nginx` namespace apply the manifest.

```
kubectl create namespace ingress-nginx
kubectl apply -f ./nginx-ingress.${APP_VERSION}.yaml
```

## Configure Features 
So far, we have successfully installed metallb and nginx ingress gateway in kubernetes data plane.    
It is time to deploy the API to the cluster. In order to create the `Deployment` and `Service` resources for the booking API, apply the `bookings.yml` provided into the `features` folder. 

```
kubectl apply -f ./features/bookings.yml
```

Lastly, create two `Ingress` resources, by applying `http-routing.yml` provided in the `features` folder. Feel free to read and understand the routing rules defined. Don' t forget to change the `host` value in every `Ingress rule`, otherwise the requests won' t reach the gateway.    
   
*You can not add the external IP of the Ingress Gateway, you have to create a domain name entry in your host file of your system, or purchase a domain and point it to the IP of your Ingress.*

```
kubectl apply -f ./features/http-routing.yml
```