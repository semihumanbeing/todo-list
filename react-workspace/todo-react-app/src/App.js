import Todo from './Todo';
import AddTodo from './AddTodo';
import './App.css';
import React from 'react';
import { call, signout } from './service/ApiService';
import {Paper, List, Container, Grid, Button, AppBar, Toolbar, Typography} from "@material-ui/core";

class App extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      items: [
      ],
      loading : true,
    };
  }

  componentDidMount(){
    call("/todo", "GET", null).then((response) => 
      this.setState({ items : response.data, loading : false })
    );
  }

  add = (item) =>{
    call("/todo", "POST", item).then((response) => 
    this.setState({ items : response.data })
    );
  };
  

  delete = (item) => {
    call("/todo", "DELETE", item).then((response) => 
    this.setState({ items : response.data })
    );
  };

  update = (item) => {
    call("/todo", "PUT", item).then((response) => 
      this.setState({ items : response.data})
    );
  }


  render(){
    var todoItems = this.state.items.length > 0 && (
      <Paper style={{margin:16}}>
        <List>
          {this.state.items.map((item, idx) => (
            <Todo 
              item={item} 
              key={item.id} 
              delete={this.delete}
              update={this.update}
            />
          ))}
        </List>
      </Paper>
    );

    var navigationBar = (
      <AppBar position="static">
        <Toolbar>
          <Grid justifyContent="space-between" container>
            <Grid item>
              <Typography variant="h6">Today's Todo List</Typography>
            </Grid>
            <Button color="inherit" onClick={signout}>
              로그아웃
            </Button>
          </Grid>
        </Toolbar>
      </AppBar>
    );

    // 로딩중이 아닐 때
    var todoListPage = (
      <div>
        {navigationBar}
        <Container maxWidth="md">
          <AddTodo add={this.add}/>
          <div className="TodoList">{todoItems}</div>
        </Container>
      </div>
    );
    
    // 로딩 중일 때 
    var loadingPage = <h1>로딩중.....</h1>;

    var content = loadingPage;

    if(!this.state.loading){
      content = todoListPage;
    }

    return <div className="App">{content}</div>;
  }
}

export default App;
