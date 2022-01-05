import React 			from 'react';
import Homescreen 		from './components/homescreen/Homescreen';
import { useQuery } 	from '@apollo/client';
import * as queries 	from './cache/queries';
import { jsTPS } 		from './utils/jsTPS';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import MapSelectScreen from './components/homescreen/MapSelectScreen';
 
const App = () => {
	let user = null;
    let transactionStack = new jsTPS();
	
    const { loading, error, data, refetch } = useQuery(queries.GET_DB_USER);

    if(error) { console.log(error); }
	if(loading) { console.log(loading); }
	if(data) { 
		let { getCurrentUser } = data;
		if(getCurrentUser !== null) { user = getCurrentUser; }
    }

	return(
		<BrowserRouter>
			<Switch>
				<Redirect exact from="/" to={ {pathname: "/home"} } />
				<Route 
					path="/home" 
					name="home" 
					render={() => 
						<Homescreen tps={transactionStack} fetchUser={refetch} user={user} />
					} 
				/>
				<Route/>

				<Route 
					path="/mapselect" 
					name="mapselect" 
					component={MapSelectScreen}
					render={() => 
						<MapSelectScreen tps={transactionStack} fetchUser={refetch} user={user} />
					} 
				/>
				<Route/>
			</Switch>
		</BrowserRouter>
	);
}

export default App;