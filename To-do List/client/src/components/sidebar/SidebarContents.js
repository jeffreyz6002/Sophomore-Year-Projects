import React            from 'react';
import SidebarHeader    from './SidebarHeader';
import SidebarList      from './SidebarList';

const SidebarContents = (props) => {
    return (
        <>
            <SidebarHeader 
                auth={props.auth} createNewList={props.createNewList} 
                activeid={props.activeid} activeList={props.activeList}
            />
            <SidebarList
                activeid={props.activeid} handleSetActive={props.handleSetActive}
                todolists={props.todolists}
                updateListField={props.updateListField}
            />
        </>
    );
};

export default SidebarContents;