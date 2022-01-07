import React                                from 'react';
import { LOGOUT }                           from '../../cache/mutations';
import { useMutation, useApolloClient }     from '@apollo/client';
import { WButton, WNavItem }                from 'wt-frontend';
import { GET_DB_USER } 				from '../../cache/queries';
import { useQuery } 		from '@apollo/client';

const LoggedIn = (props) => {
    const client = useApolloClient();
	const [Logout] = useMutation(LOGOUT);

    const handleLogout = async (e) => {
        props.LoginLogout();
        Logout();
        const { data } = await props.fetchUser();
        if (data) {
            let reset = await client.resetStore();
            if (reset) props.setActiveList({});
        }
    };

    const handleUpdate = async (e) => {
        props.updateAccount(user);
        props.setShowUpdate();
    }

    var user = null;
	const { loading, error, data, refetch } = useQuery(GET_DB_USER);
    if(data) { user = data.getCurrentUser; }
    console.log(user)

    return (
        <WNavItem hoverAnimation="lighten">
            <WButton className="navbar-options name" onClick={handleUpdate} wType="texted" hoverAnimation="text-primary">
                {user.name}
            </WButton>
            <WButton className="navbar-options" onClick={handleLogout} wType="texted" hoverAnimation="text-primary">
                Logout      
            </WButton>
        </WNavItem >
    );
};

const LoggedOut = (props) => {
    return (
        <>
            <WNavItem hoverAnimation="lighten">
                <WButton className="navbar-options-CA" onClick={props.setShowCreate} wType="texted" hoverAnimation="text-primary">
                    Create Account
                </WButton>
            </WNavItem>
            <WNavItem hoverAnimation="lighten">
                <WButton className="navbar-options" onClick={props.setShowLogin} wType="texted" hoverAnimation="text-primary"> 
                    Login 
                </WButton>
            </WNavItem>
        </>
    );
};


const NavbarOptions = (props) => {
    return (
        <>
            {
                props.auth === false ? <LoggedOut setShowLogin={props.setShowLogin} setShowCreate={props.setShowCreate} LoginLogout={props.LoginLogout} updateAccount={props.updateAccount}/>
                : <LoggedIn fetchUser={props.fetchUser} setActiveList={props.setActiveList} logout={props.logout} LoginLogout={props.LoginLogout} setShowUpdate={props.setShowUpdate} updateAccount={props.updateAccount}/>
            }
        </>

    );
};

export default NavbarOptions;