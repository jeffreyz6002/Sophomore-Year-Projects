import { useQuery } from '@apollo/client';
import React        from 'react';
import { GET_DB_TODOS } from '../../cache/queries';
import SidebarEntry from './SidebarEntry';

const SidebarList = (props) => {
    let { loading,error,data, refetch} = useQuery(GET_DB_TODOS);
    let toDoLists = [];
    if(data) { toDoLists = data.getAllTodos; }

    let newListSequence = [];
    
    for(let i = 0; i < toDoLists.length; i++){
        if(toDoLists[i].id === props.activeid){ 
            newListSequence[0] = toDoLists[i];
        }
    }
    
    let temp = 1;
        
    for(let i = 0; i < toDoLists.length; i++){
        if(toDoLists[i] !== newListSequence[0]){ 
            newListSequence[temp] = toDoLists[i];
            temp++;
        }
    }
    
    return (
        <>
            {
                newListSequence &&
                newListSequence.map(todo => (
                    <SidebarEntry
                        handleSetActive={props.handleSetActive} activeid={props.activeid}
                        id={todo.id} key={todo.id} name={todo.name} _id={todo._id}
                        updateListField={props.updateListField}
                    />
                ))
            }
        </>
    );
};

export default SidebarList;