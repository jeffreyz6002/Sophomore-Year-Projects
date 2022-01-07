import React, { useState, useEffect } 	from 'react';
import Logo 							from '../navbar/Logo';
import NavbarOptions 					from '../navbar/NavbarOptions';
import PreLogin 					from '../main/MainContents';
import SidebarContents 					from '../sidebar/SidebarContents';
import Login 							from '../modals/Login';
import MapSelectScreen					from '../homescreen/MapSelectScreen';
import Delete 							from '../modals/Delete';
import UpdateAccount 					from '../modals/UpdateAccount';
import CreateAccount 					from '../modals/CreateAccount';
import { GET_DB_TODOS } 				from '../../cache/queries';
import * as mutations 					from '../../cache/mutations';
import { useMutation, useQuery } 		from '@apollo/client';
import { WNavbar, WSidebar, WNavItem } 	from 'wt-frontend';
import { useHistory } 					from "react-router-dom";
import { WLayout, WLHeader, WLMain, WLSide } from 'wt-frontend';
import { Link, Redirect, Route } 		from 'react-router-dom';
import { UpdateListField_Transaction, 
	UpdateListItems_Transaction, 
	ReorderItems_Transaction, 
	EditItem_Transaction,
	UpdateItemField_Transaction } 				from '../../utils/jsTPS';
import WInput from 'wt-frontend/build/components/winput/WInput';


const Homescreen = (props) => {

	let todolists 							= [];
	const [activeList, setActiveList] 		= useState({});
	const [showUpdate, toggleShowUpdate] 	= useState(false);
	const [showLogin, toggleShowLogin] 		= useState(false);
	const [showCreate, toggleShowCreate] 	= useState(false);
	const [showPreLogin, toggleShowPreLogin]= useState(false);
	const [showLoggedIn, toggleLoggedIn]	= useState(false);
	const [showUserUpdate, toggleUserUpdate]= useState(false);

	const [ReorderTodoItems] 		= useMutation(mutations.REORDER_ITEMS);
	const [UpdateTodoItemField] 	= useMutation(mutations.UPDATE_ITEM_FIELD);
	const [UpdateTodolistField] 	= useMutation(mutations.UPDATE_TODOLIST_FIELD);
	const [DeleteTodolist] 			= useMutation(mutations.DELETE_TODOLIST);
	const [DeleteTodoItem] 			= useMutation(mutations.DELETE_ITEM);
	const [AddTodolist] 			= useMutation(mutations.ADD_TODOLIST);
	const [AddTodoItem] 			= useMutation(mutations.ADD_ITEM);
	const [SortBy] 					= useMutation(mutations.SORT_BY);
	const [UndoSortBy]				= useMutation(mutations.UNDO_SORT_LIST_BY);

	const { loading, error, data, refetch } = useQuery(GET_DB_TODOS);
	if(loading) { console.log(loading, 'loading'); }
	if(error) { console.log(error, 'error'); }
	if(data) { todolists = data.getAllTodos; }
	let history = useHistory();

	const auth = props.user === null ? false : true;

	const refetchTodos = async (refetch) => {
		const { loading, error, data } = await refetch();
		if (data) {
			todolists = data.getAllTodos;
			if (activeList._id) {
				let tempID = activeList._id;
				let list = todolists.find(list => list._id === tempID);
				setActiveList(list);
			}
		}
	}

	const tpsUndo = async () => {
		const retVal = await props.tps.undoTransaction();
		refetchTodos(refetch);
		return retVal;
	}

	const tpsRedo = async () => {
		const retVal = await props.tps.doTransaction();
		refetchTodos(refetch);
		return retVal;
	}

	const deleteList = async (_id) => {
		DeleteTodolist({ variables: { _id: _id }, refetchQueries: [{ query: GET_DB_TODOS }] });
		refetch();
		props.tps.clearAllTransactions();
		setActiveList({});
	};

	function useKey(key, callback){
		const callbackRef = React.useRef(callback);

		useEffect(() => {
			callbackRef.current = callback;
		})

		useEffect(() => {
			function handle(event){
				if(event.code === key){
					if(event.ctrlKey){
						callbackRef.current(event);
					}
				}
			}
			document.addEventListener("keydown", handle);
			return () => document.removeEventListener("keydown", handle)
		},[key]);
	}

	function handleUndoZ(){
		tpsUndo();
	}

	function handleRedoY(){
		tpsRedo();
	}

	useKey("KeyZ", handleUndoZ);
	useKey("KeyY", handleRedoY);


	const handleSortBy = (sortByVar) =>{
		let _id = activeList._id;
		let oldList = activeList.items; 
		
		oldList = JSON.parse(JSON.stringify(oldList))
		for(let i = 0; i < oldList.length; i++){
			delete oldList[i].__typename
		}

		let transaction = new UpdateItemField_Transaction(_id, SortBy, UndoSortBy, oldList, sortByVar);
		props.tps.addTransaction(transaction);
		tpsRedo(); 
	}

	/*
		Since we only have 3 modals, this sort of hardcoding isnt an issue, if there
		were more it would probably make sense to make a general modal component, and
		a modal manager that handles which to show.
	*/
	const setShowLogin = () => {
		toggleShowCreate(false);
		toggleShowLogin(!showLogin);
		toggleShowPreLogin(false);
	};

	const setShowCreate = () => {
		toggleShowLogin(false);
		toggleShowCreate(!showCreate);
		toggleShowPreLogin(false);
		toggleShowUpdate(false);
	};

	const setShowPreLogin = () => {
		toggleShowCreate(false);
		toggleShowLogin(false);
		toggleShowPreLogin(!showPreLogin);
		toggleShowUpdate(false);
	}

	const setShowUpdate = () =>{
		toggleShowCreate(false);
		toggleShowLogin(false);
		toggleShowPreLogin(false);
		toggleShowUpdate(!showUpdate);
	}

	const LoginLogout = () => {
		toggleLoggedIn(!showLoggedIn);
	}

	const updateAccount = (e) =>{
		toggleUserUpdate(e);
	}

	const handleToMapSelect = () =>{
		history.push("/mapselect");
	}

	return (
		<WLayout wLayout="header-lside">
			<WLHeader>
				<WNavbar color="colored">
					<ul>
						<WNavItem>
							<Logo className='logo' />
						</WNavItem>
					</ul>
					<ul>
						<NavbarOptions
							LoginLogout={LoginLogout}
							fetchUser={props.fetchUser} auth={auth} 
							setShowCreate={setShowCreate} setShowLogin={setShowLogin} 
							setShowPreLogin={setShowPreLogin} setShowUpdate={setShowUpdate}
							refetchTodos={refetch} setActiveList={setActiveList} 
							updateAccount={updateAccount}
						/>
					</ul>
				</WNavbar>
			</WLHeader>

			{ !showLoggedIn? 
				<div class="pre-login"> Welcome To The World Data Mapper </div>: 
				<MapSelectScreen> </MapSelectScreen>
			}

			{
				showCreate && (<CreateAccount fetchUser={props.fetchUser} setShowCreate={setShowCreate} LoginLogout={LoginLogout}/>)
			}

			{
				showLogin && (<Login fetchUser={props.fetchUser} refetchTodos={refetch} setShowLogin={setShowLogin} LoginLogout={LoginLogout}/>)
			}

			{
				showPreLogin && (<PreLogin fetchUser={props.fetchUser} setShowPreLogin={setShowPreLogin} />)
			}

			{
				showUpdate && (<UpdateAccount fetchUser={props.fetchUser} setShowUpdate={setShowUpdate} LoginLogout={LoginLogout}/>)
			}

		</WLayout>
	);
};

export default Homescreen;