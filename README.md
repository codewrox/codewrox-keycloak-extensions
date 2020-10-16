- [Client Auth Validation Keycloak Extension](#client-auth-validation-keycloak-extension)
- [Understanding the template](#understanding-the-template)
- [Available functions for validation](#available-functions-for-validation)
  - [Attributes](#attributes)
    - [user.getAttributeValue](#usergetattributevalue)
    - [user.hasAttribute](#userhasattribute)
  - [Groups](#groups)
    - [user.hasGroup](#userhasgroup)
  - [Roles](#roles)
    - [user.hasRole](#userhasrole)
  - [Realm Client Parameters](#realm-client-parameters)
    - [user.getClientId](#usergetclientid)
    - [user.getClientName](#usergetclientname)
    - [user.getClientBaseUrl](#usergetclientbaseurl)
    - [user.getClientRootUrl](#usergetclientrooturl)
  - [Custom Calc Values](#custom-calc-values)
    - [user.set](#userset)
- [Redirect Url Template](#redirect-url-template)
- [Installation](#installation)
  - [Configuration steps](#configuration-steps)
    - [Step-1](#step-1)
    - [Step-2](#step-2)
# Client Auth Validation Keycloak Extension

This extension provides you more granular control over the login flow for both SAML and OpenID clients.  Using this extension, one can restrict access to a particular client or set of clients based on the following validation criteria 

- User attributes 
- Group attributes
- Roles
- Groups
- Realm Client parameters

You can use any one of the criteria or in combination of multiple criteria to validate an user is allowed to login or redirect to a different location if not allowed. So setting up a criteria is very much configurable as a template. This extension offers you a custom redirect url locaton where in you can configure to redirect if the validation criteria is not met.


# Understanding the template

The template is configured after your setup your execution flow as described in the installation step below but let's understand the usage of it before then.

The template uses a keyword called "user" that is basically representing an user currently attempting to login. Technically to say, "user" is a Java object by itself that is exposed into this template for you to describe your various conditions.  So how do i use this "user" keyword in my template? Let's understand from a simple example that suppose, you want to restrict an user for a particular realm client who do not have an EmployeeID set in the user attribute.


```
 

<#if ( user.getAttributeValue("EmployeeID") != "" )  >

    ${ user.set("allow_access", true)  } 

</#if>


```


> "allow_access" is an important value to set to determine the validation pass or fail at the end


Here is the complex one where you can use logical operators like and/or/not conditions and nested "if" conditions as like below

```

<#if ( ( user.getAttributeValue("EmployeeID") != "" ) && user.hasGroup("HR-Admins") )  >

    ${ user.set("allow_access", true)  } 

<#else>

  <#if ( ( user.hasRole("Administrators") != "" ) || user.hasGroup("Keycloak-Admins") )  >

    ${ user.set("allow_access", true)  } 

   </#if>

</#if>


```


# Available functions for validation

You can use the following functions in your template for any custom validations.

##  Attributes

### user.getAttributeValue

```
Syntax:  user.getAttributeValue("<attribute_name>")

It returns empty string if the attribute not found or null

Note: if the attribute value not found for the user then it fall back on to group level attributes.  

```


### user.hasAttribute

```
Syntax:  user.hasAttribute("<attribute_name>")

It returns false if the attribute name not found.  It's good for checking an attribute is defined or not in the system.

Note: if the attribute name not found for the user then it fall back on to group level attributes to continue verify.  

```


##  Groups

### user.hasGroup


```
Syntax:  user.hasGroup("<group_name>")
where, group_name can be just a group name or it can be specified with the parent path for example, HR-Groups/Hiring-Managers

It returns false if the user who is attempting to login does not belong to the group.  



```

##  Roles

### user.hasRole


```
Syntax:  user.hasGroup("<role_name>")

It returns false if the user who is attempting to login has not been assigned to the role.  
 
Note: if the role not found for the user then it fall back on to group level roles to continue verify.  
```


##  Realm Client Parameters

### user.getClientId

```
Syntax:  user.getClientId()

It returns ID value of the client that the user is attempting to login.
 
```

### user.getClientName

```
Syntax:  user.getClientName()

It returns Name of the client that the user is attempting to login.
 
```


### user.getClientBaseUrl

```
Syntax:  user.getClientBaseUrl()

It returns base url of the client that the user is attempting to login.
 
```


### user.getClientRootUrl

```
Syntax:  user.getClientRootUrl()

It returns root url of the client that the user is attempting to login.
 
```

##  Custom Calc Values

In your template, you can set any calculated values of your choice using two utility functions called get() and set() as explained below. This can be exteremely useful for custom redirect_url formation which is explained in next topic.  For example, if an user belongs to a country then you may want to append a country code as part your redirect url dynamically per user per client basis.

### user.set

```
Syntax:  user.set("<variable_name>", <custom_value>)
where, variable_name is your identifier where the value stored into
       custom_value can be a boolean or integer or a string value(needs double quote)

It's more like a key value pair. 
 
```

# Redirect Url Template

This exension has an option to specify where to redirect if the specified validation criteria is not satisfied.  Please note that this is also a template where in you can use all the functions mentioned above.  For example,

```
Redirect url :  https://codewrox.com/${ user.get("country_code")}?employeeId=${ user.getAttributeValue("EmployeeID")}

```
 
# Installation

Simply copy the JAR into `/opt/jboss/keycloak/standalone/deployments/`

## Configuration steps

There are two scenarios to be validated 

- step-1) First time login which means browser does not have access tokens but it needs to be validated
- step-2) Already logged in with a client and has a valid access tokens available but other client needs to be validated before SSO triggers (SAML/OpenID)

### Step-1
- Clone the `Browser` authentication for example, "my-browser-flow"
- Add a new execution, select "Codewrox - Cookie Client Auth Validation" first from the drop down
- Important: Move this execution at the very top first row to have a first priorty and set to "Alternative" auth type
- Disable the "Cookie" execution as you may see this in second row 
- Now its the time to configure your newly added execution 
    - 


### Step-2
- Continue on the same setup with "my-browser-flow"
- Locate  "my-browser-flow Forms" and select actions to add a new execution but this time you select "Codewrox - Client Auth Validation"  (do not select the step#1 item)
- Important: Leave this execution at the very bottom row under the forms to have a last priorty and set to "Required" auth type
- Now its the time to configure your newly added execution 

- And finally,  do not forget to set this authentication flow in your client


--------------------------------------------------------
Hire us for SSO integrations,  Keycloak deployments 
and customizations : info(at)codewrox.com
