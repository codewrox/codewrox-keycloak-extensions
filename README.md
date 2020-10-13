# codewrox - keycloak extensions

This extension provides an authentication action that will require a user to be part of a group/role/attributes.

## Installation

Copy the JAR into `/opt/jboss/keycloak/standalone/deployments/`

## Configure

- Copy the `Browser` authentication 
- Add a new execution, select "Required Group"
- Click the newly added execution and specify the group that has access
- On your restricted clients, set this authentication


--------------------------------------------------------
Hire us for SSO integrations,  Keycloak deployments 
and customizations : sbwari at codewrox.com
